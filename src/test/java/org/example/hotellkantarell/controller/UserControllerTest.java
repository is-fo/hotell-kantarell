package org.example.hotellkantarell.controller;

import org.example.hotellkantarell.dto.RegisterRequest;
import org.example.hotellkantarell.service.UserService;
import org.example.hotellkantarell.status.RegisterStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
/*
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private final String EXISTING_EMAIL = "duplicate@example.com";

    @BeforeEach
    void setup() {
        userService.register(new RegisterRequest("Existing User", EXISTING_EMAIL, "password123"));
    }

    @Test
    void showRegisterForm_returnsRegisterView() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void register_withExistingEmail() throws Exception {
        mockMvc.perform(post("/register")
                        .param("name", "Test User")
                        .param("email", EXISTING_EMAIL)
                        .param("rawPassword", "password123")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void showLoginForm_returnsLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void login_withValidCredentials() throws Exception {
        userService.register(new RegisterRequest("Login User", "login@example.com", "password123"));

        mockMvc.perform(post("/login")
                        .param("email", "login@example.com")
                        .param("rawPassword", "password123")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/start"));
    }

    @Test
    void login_withInvalidCredentials() throws Exception {
        mockMvc.perform(post("/login")
                        .param("email", "nonexistent@example.com")
                        .param("rawPassword", "wrongpassword")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("error"));
    }*/
}
