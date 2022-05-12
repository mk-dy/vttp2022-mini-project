package sg.edu.nus.iss.vttp2022_project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import sg.edu.nus.iss.vttp2022_project.model.Recipe;
import sg.edu.nus.iss.vttp2022_project.service.RecipeService;

@Controller
public class SearchController {

    @Autowired
    private RecipeService recipeSvc;

    @GetMapping(path="/search")
    public ModelAndView searchRecipe(@RequestParam String query) {
        ModelAndView mvc = new ModelAndView();
        
        // enables queries with more than one word
        String actualQuery = query.trim();
        actualQuery = actualQuery.replaceAll("\\s", "+");
        System.out.println(actualQuery);

        List<Recipe> recipeList = recipeSvc.getRecipe(actualQuery);
        System.out.println("check >>>>>>>>>>" + recipeList.get(0).getRecipeName());
        mvc.addObject("query", query);
        mvc.addObject("recipeList", recipeList);
        mvc.setViewName("searchresults");
        return mvc;
    }

    @GetMapping(path="/search/{recipeId}")
    public ModelAndView specificRecipe(@PathVariable String recipeId) {
        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("specificrecipe");
        return mvc;
        // recipeId 
    }
}
