package au.edu.rmit.sept.webapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


//IMPORTS OFR USER SERVICE AND USER REPOSITORY

// User Model
import au.edu.rmit.sept.webapp.model.User;

public class UserController {

    // User login
    @PostMapping("/login")
    public String login(User user) {
        // Login logic

        // Once user info is recieved, user the user service to authenticate and send to the user table

        return "redirect:/home";
    }
}
