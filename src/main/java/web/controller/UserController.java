package web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import web.model.User;
import web.service.UserService;

@Controller
public class UserControllerPage {

    private final UserService userService;

    @Autowired
    public UserControllerPage(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String userHomePage(Model model, Authentication authentication) {
        User loggedInUser = (User) authentication.getPrincipal();
        model.addAttribute("user", loggedInUser);
        return "user_page";
    }
}
