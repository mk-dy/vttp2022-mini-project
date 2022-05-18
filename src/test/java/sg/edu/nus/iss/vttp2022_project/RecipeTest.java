package sg.edu.nus.iss.vttp2022_project;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import sg.edu.nus.iss.vttp2022_project.service.RecipeService;

@SpringBootTest
public class RecipeTest {
    
    @Autowired
    private RecipeService recipeSvc;

    @Test
	void getUrlForRecipeSearchShouldNotBeNull() {
        String query = "banana milkshake";
        assertNotNull(recipeSvc.getUrlForRecipeSearch(query));
    }

    @Test
	void getUrlForNextPageUsingQueryShouldNotBeNull() {
        String query = "banana milkshake";
        assertNotNull(recipeSvc.getUrlForNextPageUsingQuery(query));
    }

    @Test
	void getUrlForNextPageUsingLinkShouldNotBeNull() {
        String query = "banana milkshake";
        String link = recipeSvc.getUrlForRecipeSearch(query);
        assertNotNull(recipeSvc.getUrlForNextPageUsingLink(link));
    }

    @Test
    void getSingleRecipeShouldNotBeNull() {
        String recipeId = "35178a535499829319ce10f87dadf01f";
        assertNotNull(recipeSvc.getSingleRecipe(recipeId));
    } 

}
