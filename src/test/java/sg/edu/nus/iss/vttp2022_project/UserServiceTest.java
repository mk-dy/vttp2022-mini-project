package sg.edu.nus.iss.vttp2022_project;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import sg.edu.nus.iss.vttp2022_project.model.User;
import sg.edu.nus.iss.vttp2022_project.service.UserException;
import sg.edu.nus.iss.vttp2022_project.service.UserService;

@SpringBootTest
public class UserServiceTest {
    
    @Autowired
    private UserService userSvc;

    @Test
	void authByUsernamePassShouldReturnFalse() {
        String username = "johnnydepp";
        String password = "trial";
        boolean check = userSvc.authByUsernamePass(username, password);
        assertFalse(check);
	}

    @Test
    void addUserShouldThrowExceptionForSameEmail() throws UserException {
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
    void addUserShouldThrowExceptionForSameUsername() throws UserException {
        // database already has a user with fred as username
        // and fred@gmail.com as username
        User fred = new User();
        fred.setUsername("fred");
        fred.setEmail("antonio@gmail.com");
        fred.setName("fred");
        fred.setPassword("abc");

        assertThrows(UserException.class, () -> {userSvc.addUser(fred);});
    }

}
