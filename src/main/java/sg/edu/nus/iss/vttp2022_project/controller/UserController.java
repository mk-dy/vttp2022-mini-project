package sg.edu.nus.iss.vttp2022_project.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;

import sg.edu.nus.iss.vttp2022_project.model.ConversionUtils;
import sg.edu.nus.iss.vttp2022_project.model.User;
import sg.edu.nus.iss.vttp2022_project.repository.UserRepository;
import sg.edu.nus.iss.vttp2022_project.service.UserException;
import sg.edu.nus.iss.vttp2022_project.service.UserService;

@Controller
@RequestMapping
public class UserController {
    
    @Autowired
    private UserService userSvc;

    @Autowired
    private UserRepository userRepo;

    @GetMapping(path="/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping(path="/about")
    public String showAboutPage() {
        return "about";
    }

    @GetMapping(path="/contact")
    public String showContactPage() {
        return "contact";
    }

    @GetMapping("/logout")
    public String getLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping(path="/auth")
    public ModelAndView getLogin(@RequestBody MultiValueMap<String,String> payload,
        HttpSession session) {

        ModelAndView mvc = new ModelAndView();
        String username = payload.getFirst("username");
        String password = payload.getFirst("password");
        if (!userSvc.authByUsernamePass(username, password)) {
            // error, prompt user to try again
            mvc.setViewName("loginerror");
            mvc.setStatus(HttpStatus.FORBIDDEN);
        }
        Optional<User> optUser = userRepo.returnUser(username, password);
        User user = optUser.get();
        session.setAttribute("username", username);
        session.setAttribute("userId", user.getUserId());
        // mvc.addObject("username", optUser.get().getUsername());
        mvc.setStatus(HttpStatus.OK);
        mvc.setViewName("mainpage");
        
        return mvc;
    }

    @GetMapping(path="/signup")
    public ModelAndView showSignupPage() {
        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("signup");
        return mvc;
    }

    @PostMapping(path="/createaccount") 
    public ModelAndView createAccount(@RequestBody MultiValueMap<String,String> payload) {
        ModelAndView mvc = new ModelAndView();
        User userFromForm = ConversionUtils.populateFromForm(payload);
        try {
            userSvc.addUser(userFromForm);
            mvc.addObject("message", "User %s has been created successfully!".formatted(userFromForm.getUsername()));
            mvc.setViewName("usercreationsuccess");
        } catch (UserException e) {
            mvc.setStatus(HttpStatus.BAD_REQUEST);
            mvc.addObject("message", "Error - %s".formatted(e.getReason()));
            mvc.setViewName("usercreationerror");
            e.printStackTrace();
        }
        return mvc;
    }    
    





}
