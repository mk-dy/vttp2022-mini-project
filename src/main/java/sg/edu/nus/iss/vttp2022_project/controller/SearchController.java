package sg.edu.nus.iss.vttp2022_project.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import sg.edu.nus.iss.vttp2022_project.model.Recipe;
import sg.edu.nus.iss.vttp2022_project.service.RecipeService;

@Controller
@RequestMapping(path="/home")
public class SearchController {

    @Autowired
    private RecipeService recipeSvc;

    @GetMapping(path="/search")
    public ModelAndView searchRecipe(@RequestParam String query) {
        ModelAndView mvc = new ModelAndView();
        List<Recipe> recipeList = new LinkedList<>();
        String currentPageUrl = recipeSvc.getUrlForRecipeSearch(query);
        recipeList = recipeSvc.getRecipeList(currentPageUrl);
        String nextPageUrl = recipeSvc.getUrlForNextPageUsingLink(currentPageUrl);
        mvc.addObject("query", query);
        mvc.addObject("recipeList", recipeList);
        mvc.addObject("nextPageUrl", nextPageUrl);
        mvc.addObject("currentPageUrl", currentPageUrl);
        mvc.setViewName("searchresults");
        return mvc;
        
    }

    @PostMapping(path="/search")
    public ModelAndView searchRecipeWithNextPage(
        @RequestBody MultiValueMap<String,String> payload) {

        ModelAndView mvc = new ModelAndView();
        List<Recipe> recipeList = new LinkedList<>();
        String subsequentPageUrl;

        String query = payload.getFirst("query");
        // String previousPageUrl;
        // this previousPageUrl is not null only when it came from GetMapping
        // otherwise this previousPageUrl is still NULL
        // session.setAttribute("previousPageUrl", payload.getFirst("previousPageUrl"));
        // if (payload.getFirst("previousPageUrl") != null) {
        //     previousPageUrl = payload.getFirst("previousPageUrl"); // previous currenturl
        // } else {
        // // nextpage will be previouspage in time
        // previousPageUrl  = payload.getFirst("nextPageUrl");
        // }
        // previousPageUrl = (String) session.getAttribute("previousPageUrl");

        String nextPageUrl = payload.getFirst("nextPageUrl");

        System.out.println(">>> query:" + query);
        System.out.println(">>> nextPageUrl:" + nextPageUrl); // when i click previous button, nextPageurl will be null
        // System.out.println(">>> previousPageUrl:"+ previousPageUrl); // or rather the currentPageUrl again
        
        // i.e. when previous button is clicked 
        // because there is no payload with "nextPageUrl" in the form
        // if (payload.getFirst("nextPageUrl") == null) {
        //     // my previousPageUrl's recipeList should be returned and 
        //     // the nextPageUrl should be regenerated based on previousPageUrl
        //     recipeList = recipeSvc.getRecipeList(previousPageUrl);
        //     subsequentPageUrl = recipeSvc.getUrlForNextPageUsingLink(previousPageUrl);
        //     mvc.addObject("nextPageUrl", subsequentPageUrl);
            
        //     // if previousPageUrl does not contain "&_cont="
        //     // grey out the previous button
        //     if (!previousPageUrl.contains("&_cont=")) {
        //         previousPageUrl = null;
        //         mvc.addObject("nextPageUrl", subsequentPageUrl);
        //         mvc.addObject("previousPageUrl", previousPageUrl);
        //         mvc.addObject("query", query);
        //         mvc.addObject("recipeList", recipeList);
        //         mvc.setViewName("searchresults");
        //         return mvc;
        //     }
        //     mvc.addObject("previousPageUrl", previousPageUrl);
        //     mvc.addObject("query", query);
        //     mvc.addObject("recipeList", recipeList);
        //     mvc.setViewName("searchresults");
        //     return mvc;

        // }
        // upon clicking next page, populate html with page 2 results
        recipeList = recipeSvc.getRecipeList(nextPageUrl);
        // also prepare the page 3 url to be used as next page for page 2
        subsequentPageUrl = recipeSvc.getUrlForNextPageUsingLink(nextPageUrl);
        // System.out.println(recipeList.get(0).getRecipeName());
        mvc.addObject("nextPageUrl", subsequentPageUrl);
        // mvc.addObject("previousPageUrl", previousPageUrl);
        mvc.addObject("query", query);
        mvc.addObject("recipeList", recipeList);
        mvc.setViewName("searchresults");
        return mvc;
    }

    

}
