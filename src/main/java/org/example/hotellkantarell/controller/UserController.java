package org.example.hotellkantarell.controller;

import jakarta.servlet.http.HttpSession;
import org.example.hotellkantarell.dto.LoginRequest;
import org.example.hotellkantarell.dto.RegisterRequest;
import org.example.hotellkantarell.model.User;
import org.example.hotellkantarell.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute RegisterRequest registerRequest) {
        return userService.register(registerRequest) != null ? "redirect:/login" : "redirect:/register";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute LoginRequest loginRequest, HttpSession session) {
        User user = userService.login(loginRequest);
        if (user != null) {
            session.setAttribute("user", user);
            return "start";
        }
        return "redirect:/login";
    }

}
