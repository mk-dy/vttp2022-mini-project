package sg.edu.nus.iss.vttp2022_project.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import sg.edu.nus.iss.vttp2022_project.service.RecipeService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
public class SearchControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @Autowired
    private RecipeService recipeSvc;

    @Test
    public void getSearchTestShouldReturn200() {

        RequestBuilder req = MockMvcRequestBuilders.get("/home/search")
                .queryParam("query", "banana")
                .sessionAttr("username","test");
        
        //call controller
        MvcResult result = null;
        
        try {
            result = mvc.perform(req).andReturn();

        } catch (Exception e) {
            fail("cannot perform mvc invocation", e);
            return;
        }

        // Get response
        MockHttpServletResponse resp = result.getResponse();
        
        try {
            String payload = resp.getContentAsString();
            assertNotNull(payload);

        } catch (Exception e) {
            fail("cannot retrieve response payload", e);
            return;
        }
    }

    // @Test
    // public void postSearchTestShouldReturn200() {

    //     MultiValueMap<String,String> form = new LinkedMultiValueMap<>();
    //     String searchQuery = "banana";
    //     form.add("query", searchQuery);
    //     form.add("nextPageUrl", 
    //     recipeSvc.getUrlForNextPageUsingLink(recipeSvc.getUrlForRecipeSearch(searchQuery)));

    //     RequestBuilder req = MockMvcRequestBuilders.post("/home/search")
    //             .params(form)
    //             .sessionAttr("username","johndoe");
        
    //     try {
    //         mvc.perform(req).andExpect(status().isOk());
    //     } catch (Exception e) {
    //         fail("cannot perform mvc invocation", e);
    //         return;
    //     }

    // }
}
