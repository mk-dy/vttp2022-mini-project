package sg.edu.nus.iss.vttp2022_project.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import sg.edu.nus.iss.vttp2022_project.model.Recipe;
import sg.edu.nus.iss.vttp2022_project.service.RecipeService;

@Controller
public class SearchController {

    @Autowired
    private RecipeService recipeSvc;

    @GetMapping(path="/search")
    public ModelAndView searchRecipe(@RequestParam String query, HttpSession session) {
        ModelAndView mvc = new ModelAndView();
        
        // enables queries with more than one word
        String actualQuery = query.trim();
        actualQuery = actualQuery.replaceAll("\\s", "+");
        System.out.println(actualQuery);

        List<Recipe> recipeList = recipeSvc.getRecipeList(actualQuery);
        System.out.println("check >>>>>>>>>>" + recipeList.get(0).getRecipeName());
        mvc.addObject("query", query);
        mvc.addObject("recipeList", recipeList);
        mvc.setViewName("searchresults");

        return mvc;
    }

    @GetMapping(path="/search/recipe")
    public ModelAndView getSpecificRecipe(@RequestParam String recipeId) {
        System.out.println(recipeId);
        Recipe recipe = recipeSvc.getSingleRecipe(recipeId);
        System.out.println("check >>>>>>>>" + recipe.getRecipeName());
        ModelAndView mvc = new ModelAndView();
        mvc.addObject("recipe", recipe);
        mvc.setViewName("specificrecipe");

        return mvc;
    }

    // needs a postmapping for the like button
    @PostMapping(path="/search/recipe")
    public ModelAndView postLikedRecipe(@RequestBody MultiValueMap<String,String> payload, HttpSession session) {
        String recipeId = payload.getFirst("recipeId");
        Recipe recipe = recipeSvc.getSingleRecipe(recipeId);
        System.out.println("check recipe name >>>>>>>>" + recipe.getRecipeName());
        Integer userId = (Integer) session.getAttribute("userId");
        System.out.println("check user >>>>>>>>" + userId);
        ModelAndView mvc = new ModelAndView();
        // to insert into likes table, we need userId, recipeId, recipeName
        boolean check = recipeSvc.insertLikedRecipe(recipe, userId);
        System.out.println("check >>>>>>>>" + check); 
        mvc.addObject("recipe", recipe);
        
        if (check == false) {
            mvc.setViewName("specificrecipe");
            mvc.addObject("message", "Recipe has already been saved previously.");
        }
        mvc.setViewName("specificrecipe");
        mvc.addObject("message", "Recipe has been favourited!");
        
        return mvc;
    }

    @GetMapping(path="/liked")
    public ModelAndView getLikedRecipes() {
        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("likedrecipe");
        return mvc;
    }
    // subsequently we need a likes page to generate what we have liked
}
