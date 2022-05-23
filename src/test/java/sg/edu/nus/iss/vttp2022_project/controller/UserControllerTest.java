package sg.edu.nus.iss.vttp2022_project.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @Autowired
    private JdbcTemplate template;

    @Test
    public void loginTest() {
        RequestBuilder req = MockMvcRequestBuilders.get("/login")
                // .sessionAttr("username","test")
                .accept(MediaType.TEXT_HTML_VALUE);
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

    @Test
    public void aboutTest() {

        RequestBuilder req = MockMvcRequestBuilders.get("/about")
                // .sessionAttr("username", "test")
                .accept(MediaType.TEXT_HTML_VALUE);
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

    @Test
    public void contactTest() {
        
        RequestBuilder req = MockMvcRequestBuilders.get("/contact")
                // .sessionAttr("username", "test")
                .accept(MediaType.TEXT_HTML_VALUE);
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

    @Test
    public void logoutTestShouldReturn200() {
        RequestBuilder req = MockMvcRequestBuilders.get("/logout");
                // .sessionAttr("username", "test");
        
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

    @Test
    public void authTestShouldShouldReturn403() {   
        MultiValueMap<String,String> form = new LinkedMultiValueMap<>();
        form.add("email", "pepepe@gmail.com");
        form.add("username", "pepepe");
        form.add("name", "Pe Pe Pe");
        form.add("password", "abc");
        
        RequestBuilder req = MockMvcRequestBuilders.post("/auth")
                        .params(form)
                        .sessionAttr("username", "pepepe")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED);        
                        
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
        assertEquals(403,resp.getStatus());
    }

    @Test
    public void authTestShouldShouldReturn200() {   
        MultiValueMap<String,String> form = new LinkedMultiValueMap<>();
        form.add("username", "johndoe");
        form.add("password", "abc");
        
        RequestBuilder req = MockMvcRequestBuilders.post("/auth")
                        .params(form)
                        .sessionAttr("username", "johndoe")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED);        
                        
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
        assertEquals(302,resp.getStatus());
        // since redirect to home
    }

    @Test
    public void homeTest() {
        
        RequestBuilder req = MockMvcRequestBuilders.get("/home")
                // .sessionAttr("username", "test")
                .accept(MediaType.TEXT_HTML_VALUE);
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




    @Test
    public void signupTest() {
        RequestBuilder req = MockMvcRequestBuilders.get("/signup")
                // .sessionAttr("username", "test")
                .accept(MediaType.TEXT_HTML_VALUE);
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

    @Test
    public void createaccountTestShouldWork() {
        
        MultiValueMap<String,String> form = new LinkedMultiValueMap<>();
        form.add("email", "pepepe@gmail.com");
        form.add("username", "pepepe");
        form.add("name", "Pe Pe Pe");
        form.add("password", "abc");
        // weird that i have to set sessionattr("username") considering
        // that this url does not have to pass through the filter.
        RequestBuilder req = MockMvcRequestBuilders.post("/createaccount")
                        .params(form)
                        // .sessionAttr("username", "pepepe")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED);        
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
            System.out.println(">>>>>>> payload: " + payload);
            assertNotNull(payload);
        } catch (Exception e) {
            fail("cannot retrieve response payload", e);
            return;
        }
    }

    @AfterEach
    public void deletePepepeFromUser() {
		int count = template.update("delete from user where username = ?","pepepe");
	}

    @Test
    public void createaccountTestShouldNotWork() {
        
        MultiValueMap<String,String> form = new LinkedMultiValueMap<>();
        form.add("email", "test@gmail.com");
        form.add("username", "test");
        form.add("name", "test");
        form.add("password", "abc");
        // weird that i have to set sessionattr("username") considering
        // that this url does not have to pass through the filter.
        RequestBuilder req = MockMvcRequestBuilders.post("/createaccount")
                        .params(form)
                        // .sessionAttr("username", "test")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED);        
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
        assertEquals(400,resp.getStatus());
    }





    

}
