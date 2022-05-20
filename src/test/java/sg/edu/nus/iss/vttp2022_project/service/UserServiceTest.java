package sg.edu.nus.iss.vttp2022_project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import sg.edu.nus.iss.vttp2022_project.model.ConversionUtils;
import sg.edu.nus.iss.vttp2022_project.model.User;
import sg.edu.nus.iss.vttp2022_project.repository.UserRepository;
import sg.edu.nus.iss.vttp2022_project.service.UserException;
import sg.edu.nus.iss.vttp2022_project.service.UserService;

@SpringBootTest
public class UserServiceTest {
    
    @Autowired
    private UserService userSvc;

    @Autowired
    private UserRepository userRepo;

    @Autowired
	private JdbcTemplate template;

    @Test
	public void authByUsernamePassShouldReturnFalse() {
        String username = "johnnydepp";
        String password = "trial";
        boolean check = userSvc.authByUsernamePass(username, password);
        assertFalse(check);
	}

    @Test
    public void addUserShouldThrowExceptionForSameEmail() throws UserException {
        // database already has a user with fred as username
        // and fred@gmail.com as username
        User fred = new User();
        fred.setUsername("antonio");
        fred.setEmail("fred@gmail.com");
        fred.setName("fred");
        fred.setPassword("abc");

        assertThrows(UserException.class, () -> {userSvc.addUser(fred);});
    }

    @Test
    public void addUserShouldThrowExceptionForSameUsername() throws UserException {
        // database already has a user with fred as username
        // and fred@gmail.com as username
        User fred = new User();
        fred.setUsername("fred");
        fred.setEmail("antonio@gmail.com");
        fred.setName("fred");
        fred.setPassword("abc");

        assertThrows(UserException.class, () -> {userSvc.addUser(fred);});
    }

    @Test
	public void insertUserShouldSucceed() {
        User user = new User();
        user.setUsername("johnjohnjohn");
        user.setEmail("johnjohnjohn@gmail.com");
        user.setName("JohnJohnJohn");
        user.setPassword("abc");

        boolean check = userRepo.insertUser(user);
        assertTrue(check);
	}

    @AfterEach
    public void deleteUser() {
		int count = template.update("delete from user where username = ?","johnjohnjohn");
	}
    
    @Test
    public void userPopulation() {
        MultiValueMap<String,String> form = new LinkedMultiValueMap<>();  
        form.add("email", "dondondon@gmail.com");
        form.add("username", "dondondon");
        form.add("name", "Don Don Don");
        form.add("password", "abc");
        User user = ConversionUtils.populateFromForm(form);

        assertEquals("dondondon", user.getUsername());
    }

}
