package sg.edu.nus.iss.vttp2022_project.controller;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import sg.edu.nus.iss.vttp2022_project.model.Recipe;
import sg.edu.nus.iss.vttp2022_project.service.RecipeService;

@Controller
@RequestMapping(path="/home")
public class RecipeController {
    
    @Autowired
    private RecipeService recipeSvc;

    // shows specific recipe
    @GetMapping(path="/recipe/{recipeId}")
    public ModelAndView getSpecificRecipe(@PathVariable String recipeId) {
        System.out.println(recipeId);
        Recipe recipe = recipeSvc.getSingleRecipe(recipeId);
        System.out.println("check >>>>>>>>" + recipe.getRecipeName());
        ModelAndView mvc = new ModelAndView();
        mvc.addObject("recipe", recipe);
        mvc.setViewName("specificrecipe");

        return mvc;
    }

    // for the like button
    @PostMapping(path="/recipe/{recipeId}")
    public ModelAndView postLikedRecipe(@PathVariable String recipeId, HttpSession session) {

        ModelAndView mvc = new ModelAndView();
        // String recipeId = payload.getFirst("recipeId");
        Recipe recipe = recipeSvc.getSingleRecipe(recipeId);
        System.out.println("check recipe name >>>>>>>>: " + recipe.getRecipeName());
        Integer userId = (Integer) session.getAttribute("userId");
        System.out.println("check user >>>>>>>>: " + userId);
        // if (userId == null) {
        //     mvc.setViewName("redirect:/");
        //     return mvc;
        // }
        boolean check = recipeSvc.insertLikedRecipe(recipe, userId);
        System.out.println("check >>>>>>>>: " + check); 
        mvc.addObject("recipe", recipe);
        
        if (check == false) {
            mvc.setViewName("specificrecipe");
            mvc.addObject("message", "You have already liked this recipe previously!");
            mvc.addObject("check", check);
            return mvc;
        }
        mvc.setViewName("specificrecipe");
        mvc.addObject("message", "Liked!");
        mvc.addObject("check", check);
        return mvc;
    }

    // liked recipes page
    @GetMapping(path="/{userId}/likedrecipes")
    public ModelAndView getLikedRecipes(@PathVariable Integer userId, HttpSession session) {

        ModelAndView mvc = new ModelAndView();
        // if (session.getAttribute("username") == null) {
        //     mvc.setViewName("redirect:/");
        //     return mvc;
        // }
        List<Recipe> recipeList = recipeSvc.getLikedRecipes(userId);
        mvc.addObject("recipeList", recipeList);
        mvc.addObject("userId", userId);
        mvc.setViewName("likedrecipe");
        return mvc;
    }
    
    // deletion of liked recipe
    @PostMapping(path="/{userId}/likedrecipes")
    public ModelAndView deleteLikedRecipes(@PathVariable Integer userId, 
    @RequestBody MultiValueMap<String,String> payload, HttpSession session) {
        
        ModelAndView mvc = new ModelAndView();
        String recipeId = payload.getFirst("recipeId");
        System.out.println(recipeId);
        boolean check = recipeSvc.deleteLikedRecipe(recipeId);
        System.out.println(check);

        mvc.setViewName("redirect:/home/" + userId + "/likedrecipes");
        return mvc;

    } 
}
