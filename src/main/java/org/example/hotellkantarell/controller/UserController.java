package org.example.hotellkantarell.controller;

import jakarta.servlet.http.HttpSession;
import org.example.hotellkantarell.dto.LoginRequest;
import org.example.hotellkantarell.dto.RegisterRequest;
import org.example.hotellkantarell.model.User;
import org.example.hotellkantarell.repository.UserRepository;
import org.example.hotellkantarell.service.UserService;
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
        User user = userService.register(registerRequest);
        if (user == null) {
            model.addAttribute("error", "Det finns redan ett konto med den emailen eller så är det kaffe i servern.");
            return "register";
        } else {
            redirectAttributes.addFlashAttribute("success", "Du är nu registrerad!");
            return "redirect:/login";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute LoginRequest loginRequest, RedirectAttributes redirectAttributes, HttpSession session) {
        User user = userService.login(loginRequest);
        if (user != null) {
            session.setAttribute("user", user);
            redirectAttributes.addFlashAttribute("success", "Välkommen " + user.getName() + "!");
            return "redirect:/start";
        }
        redirectAttributes.addFlashAttribute("error", "Fel användarnamn eller lösenord");
        return "redirect:/login";
    }

}
