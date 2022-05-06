package sg.edu.nus.iss.vttp2022_project.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import sg.edu.nus.iss.vttp2022_project.model.User;
import sg.edu.nus.iss.vttp2022_project.repository.UserRepository;

@Controller
@RequestMapping
public class UserController {
    
    @Autowired
    private UserRepository userRepo;

    @GetMapping(path="/login")
    public ModelAndView getLogin(@RequestBody MultiValueMap<String,String> payload) {
        String email = payload.getFirst("email");
        String password = payload.getFirst("password");
        Optional<User> userOpt = userRepo.returnUser(email, password);
        ModelAndView mvc = new ModelAndView();

        if (userOpt.isEmpty()) {
            // error, prompt user to create an account
        }
        mvc.setStatus(HttpStatus.OK);
        mvc.setViewName("mainpage");
        return mvc;
    }

    // another page for upon successful login





}
