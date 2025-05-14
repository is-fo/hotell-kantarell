package org.example.hotellkantarell.controller;

import jakarta.validation.Valid;
import org.example.hotellkantarell.dto.LoginRequest;
import org.example.hotellkantarell.dto.RegisterRequest;
import org.example.hotellkantarell.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    String registerUser(@Valid @RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @GetMapping("/login")
    String loginUser(@RequestBody LoginRequest request) {
        return userService.login(request);
    }
}
