package sg.edu.nus.iss.vttp2022_project.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.vttp2022_project.model.User;
import sg.edu.nus.iss.vttp2022_project.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepo;

    // authenticate
    public boolean authByUsernamePass(String username, String password) {
        Integer count = userRepo.checkUserByUsernamePass(username, password);

        return count == 1;
    }

    // need to consider for scenarios where email already exists 
    // or username already exists
    public void addUser(User user) throws UserException {

        Optional<User> optUserByEmail = userRepo.checkUserByEmail(user.getEmail());
        Optional<User> optUserByUsername = userRepo.checkUserByUsername(user.getUsername());
        if (optUserByEmail.isPresent()) {
            throw new UserException("There is already a user registered with the email: %s.".formatted(user.getEmail()));
        }

        if (optUserByUsername.isPresent()) {
            throw new UserException("There is already a user registered with the username, %s".formatted(user.getUsername()));
        }

        if (!userRepo.insertUser(user)) {
            throw new UserException("Unable to add %s as a user".formatted(user.getUsername()));
        }

    }
}
