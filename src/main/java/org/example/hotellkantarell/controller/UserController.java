package org.example.hotellkantarell.controller;

import jakarta.servlet.http.HttpSession;
import org.example.hotellkantarell.dto.LoginRequest;
import org.example.hotellkantarell.dto.RegisterRequest;
import org.example.hotellkantarell.dto.UserDto;
import org.example.hotellkantarell.service.UserService;
import org.example.hotellkantarell.status.RegisterStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String registerUser(@ModelAttribute RegisterRequest registerRequest, RedirectAttributes redirectAttributes, Model model) {

        final var result = userService.register(registerRequest);
        if (result.equals(RegisterStatus.SUCCESS)) {
            redirectAttributes.addFlashAttribute("success", result.getMessage());
            return "redirect:/login";
        } else {
            model.addAttribute("error", result.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute LoginRequest loginRequest, RedirectAttributes redirectAttributes, HttpSession session) {
        UserDto user = userService.login(loginRequest);
        if (user != null) {
            session.setAttribute("user", user);
            redirectAttributes.addFlashAttribute("success", "Välkommen " + user.name() + "!");
            return "redirect:/start";
        }
        redirectAttributes.addFlashAttribute("error", "Fel användarnamn eller lösenord");
        return "redirect:/login";
    }

}
