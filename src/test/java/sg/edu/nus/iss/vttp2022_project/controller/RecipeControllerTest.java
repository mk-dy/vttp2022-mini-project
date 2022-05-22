package sg.edu.nus.iss.vttp2022_project.controller;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
public class RecipeControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @Autowired
    private JdbcTemplate template;

    @Test
    public void getSpecificRecipeTestStatusShouldBeOk() {
        RequestBuilder req = MockMvcRequestBuilders.get("/home/recipe/{recipeId}","e553e3f009016f8855428ed0547201d1")
                        .sessionAttr("username", "test")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED);                        
        
        try {
            mvc.perform(req).andExpect(status().isOk());
        } catch (Exception e) {
            fail("cannot perform mvc invocation", e);
            return;
        }
    }

    @Test
    public void postSpecificRecipeTestShouldNotWork() {
        // userId 7 belongs to username pell, it should already contain the following recipe
        RequestBuilder req = MockMvcRequestBuilders.post("/home/recipe/{recipeId}","e553e3f009016f8855428ed0547201d1")
                        .sessionAttr("userId", 7)
                        .sessionAttr("username", "pell")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED);                        
        
        try {
            mvc.perform(req).andExpect(content().string(containsString("You have already liked this recipe previously!")));
        } catch (Exception e) {
            fail("cannot perform mvc invocation", e);
            return;
        }
    }

    @Test
    public void postSpecificRecipeTestShouldWork() {
        // userId 4 belongs to username test, it should not contain the following recipe
        RequestBuilder req = MockMvcRequestBuilders.post("/home/recipe/{recipeId}","e553e3f009016f8855428ed0547201d1")
                        .sessionAttr("userId", 4)
                        .sessionAttr("username", "test")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED);                        
        
        try {
            mvc.perform(req).andExpect(content().string(containsString("Liked!")));
        } catch (Exception e) {
            fail("cannot perform mvc invocation", e);
            return;
        }
    }

    @AfterEach
    public void deleteForTest() {
        int count = template.update("delete from likes where user_id = ?",4);

    }
    
    @Test
    public void getLikedRecipesTestStatusShouldBeOk() {
        // username johndoe already has liked recipes
        RequestBuilder req = MockMvcRequestBuilders.get("/home/{userId}/likedrecipes",1)
                        .sessionAttr("username", "johndoe")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED);                        
        
        try {
            mvc.perform(req).andExpect(status().isOk());
        } catch (Exception e) {
            fail("cannot perform mvc invocation", e);
            return;
        }
    }
}
