package sg.edu.nus.iss.vttp2022_project.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;

import sg.edu.nus.iss.vttp2022_project.model.Recipe;
import sg.edu.nus.iss.vttp2022_project.repository.RecipeRepository;
import sg.edu.nus.iss.vttp2022_project.service.RecipeService;

@SpringBootTest
public class RecipeServiceTest {
    
    @Autowired
    private RecipeService recipeSvc;

    @Autowired
    private JdbcTemplate template;

    @Test
	public void getUrlForRecipeSearchShouldNotBeNull() {
        String query = "banana milkshake";
        assertNotNull(recipeSvc.getUrlForRecipeSearch(query));
    }

    @Test
	public void getUrlForNextPageUsingQueryShouldNotBeNull() {
        String query = "banana milkshake";
        assertNotNull(recipeSvc.getUrlForNextPageUsingQuery(query));
    }

    @Test
	public void getUrlForNextPageUsingLinkShouldNotBeNull() {
        String query = "banana milkshake";
        String link = recipeSvc.getUrlForRecipeSearch(query);
        assertNotNull(recipeSvc.getUrlForNextPageUsingLink(link));
    }

    @Test
    public void getSingleRecipeShouldNotBeNull() {
        String recipeId = "35178a535499829319ce10f87dadf01f";
        assertNotNull(recipeSvc.getSingleRecipe(recipeId));
    } 

    @Test
    public void getRecipeListShouldNotBeNull() {
        String query = "banana milkshake";
        String link = recipeSvc.getUrlForRecipeSearch(query);
        assertNotNull(recipeSvc.getRecipeList(link));
    }
    
    @Test
    public void getLikedRecipesShouldReturnOptionalEmpty() {
        Integer userId = -300;
        assertNull(recipeSvc.getLikedRecipes(userId));
    }

    @Test
    public void getLikedRecipesShouldReturn200() {
        // userId 7 belongs to username with "pell"
        // pell's likes is not empty
        Integer userId = 7;
        assertNotNull(recipeSvc.getLikedRecipes(userId));
    }

    @Test
    public void insertLikedRecipeShouldReturnTrue() {
        // test's userId is 4 and test has not liked any recipe in database
        Integer userId = 4;
        Recipe recipe = new Recipe();
        recipe.setRecipeId("randomtest");
        recipe.setRecipeName("randomtest");
        recipe.setImageLink("randomtest");
        assertTrue(recipeSvc.insertLikedRecipe(recipe, userId));

    }

    @AfterEach
    public void deleteRecipeFromTest() {
		int count = template.update("delete from likes where user_id = ?","4");
	}

    @Test
    public void insertLikedRecipeShouldReturnFalse() {
        // fred's userId is 3 and fred already has one liked recipe in the database
        Integer userId = 3;
        Recipe recipe = new Recipe();
        recipe.setRecipeId("7e3aad53d00b9acccaef151afa67b7e6");
        recipe.setRecipeName("Perfect Porterhouse Steak recipes");
        recipe.setImageLink("https://edamam-product-images.s3.amazonaws.com/web-img/d30/d30083c0e56c915a136b8637e623ef3e?X-Amz-Security-Token=IQoJb3JpZ2luX2VjEPn%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FwEaCXVzLWVhc3QtMSJGMEQCIFRxmV63JRCBAzGU3KnfxUtxT3%2BO2BZ2DwdCpPtPLKu9AiAJ%2BOXnEpYE%2FdWDn34ShQeA1ZCPpibwwt9GJW8iS%2BO%2FVSrbBAjS%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F8BEAAaDDE4NzAxNzE1MDk4NiIM9e%2BKUWoxcM9TV3hyKq8EgJF4yfXciBvw4XVHP90bf24yvChTDm34E3bpj6A5FW6zZ336lFShJngAu0tfaTelW7IdUQ1%2Bvbb3fg4V4neKwWwi4%2FqxPGjHv7q8EpVUpBh0fKXGL7xurQTLfglV8zM1gvuAlXhwo0P24RvFcjj3eZ6x1w9DkA8%2FaTQd%2BJv97c%2BqPbm7klkiNi4VfLOYHwhr%2B0PMHsWdtf%2BwQYiLSWRKvN0w9FPvmY%2Fld5xG%2Bkv79JfjU%2BrI21MdKSOQJMuhU9vCB7I%2FZc3Jiwcwj%2BVFwxHc98fN8P56iGYRq5KUufisSTEhVjtE32dUuNX5923eXeaEuh3g28thg%2BDY8q3QZnZoHajd7n0huwzPGaaWCVGhoPN9yAEQA7UMSR6chjS1acgKbSgtncBrY%2FR7S8SgGqGt0TGnKj8V%2F3JYQkMnjiLbfpExOCtYlOsM5psJ2%2FNGmaUlQKlHAV%2BRMPb7JiL2p4Y0mVGjw41alnvVrSQkpAbW62dvLhUVgpxkZ%2BFtOX4yy3DZi48KV0uWOO5I2cAMHaR5CiMY5TNefizmJ1iMr8csCjt%2FhAHoQtPxu%2FnK3mjbdNciN8pEeLbzlsnxiyjAXxwGJ0dcT9YSjlPHYPxRPuZ3PU37nRZ8vl9tj6surYAFbmmGhLPsYXvrBQEYAqDj9XsZMRzUSrxpMQDbpRzUYPAoBtuLpPhl1Wsws6wGtbuED04aTt1a6CN%2BhPNE3If3XgQpyQDZkQ0VG%2FhOwqrPlo4IJjCW55KUBjqqAUFv31eJv%2B7YfMQ%2BfQQCV4acAoFfJrm9IKNPgL2I%2BKzxEHBPhXefvslcHvp0oRbMKzYGK42SPuiLXjpcowhyAVAB5AiRAg8Ts88477p4zvvyasG%2FxU1czjJne7TUO%2FJexqxEiBcDIo159r6Q%2BEFmKUECtBQbpbBNKsaWfwoqlHI9%2F3Clhvef6QV2ocOuRFqRpUWeJFJD4o0lUUTkHCq5%2F0bJ3beFAIpm3wmw&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20220518T095656Z&X-Amz-SignedHeaders=host&X-Amz-Expires=3600&X-Amz-Credential=ASIASXCYXIIFM4QCM6V7%2F20220518%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Signature=7d9810ccc2da85f7b2e5b4f6bd631f89acc045abd43bde1bb4ad16024119cdb5");
        assertFalse(recipeSvc.insertLikedRecipe(recipe, userId));

    }

}
