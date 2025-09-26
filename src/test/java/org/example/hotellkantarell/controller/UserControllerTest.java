package org.example.hotellkantarell.controller;

import org.example.hotellkantarell.dto.LoginRequest;
import org.example.hotellkantarell.dto.RegisterRequest;
import org.example.hotellkantarell.dto.UserDto;
import org.example.hotellkantarell.service.UserService;
import org.example.hotellkantarell.status.RegisterStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private HttpSession session;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void showRegisterForm_returnsRegisterView() {
        String viewName = userController.showRegisterForm();
        assertEquals("register", viewName);
    }

    @Test
    void registerUser_withSuccess() {
        RegisterRequest request = new RegisterRequest("Test Testsson", "test@testsson.com", "password");
        when(userService.register(any(RegisterRequest.class))).thenReturn(RegisterStatus.SUCCESS);

        String viewName = userController.registerUser(request, redirectAttributes, model);

        assertEquals("redirect:/login", viewName);
        verify(redirectAttributes).addFlashAttribute("success", RegisterStatus.SUCCESS.getMessage());
        verify(model, never()).addAttribute(eq("error"), anyString());
    }

    @Test
    void registerUser_withFailure() {
        RegisterRequest request = new RegisterRequest("", "test@testsson.com", "password");
        when(userService.register(any(RegisterRequest.class))).thenReturn(RegisterStatus.MISSING_NAME);

        String viewName = userController.registerUser(request, redirectAttributes, model);

        assertEquals("register", viewName);
        verify(model).addAttribute("error", RegisterStatus.MISSING_NAME.getMessage());
        verify(redirectAttributes, never()).addFlashAttribute(anyString(), anyString());
    }

    @Test
    void showLoginForm_returnsLoginView() {
        String viewName = userController.showLoginForm();
        assertEquals("login", viewName);
    }

    @Test
    void loginUser_withValidCredentials() {
        LoginRequest request = new LoginRequest("test@testsson.com", "password");
        UserDto userDto = new UserDto(1L, "Test Testsson", "test@testsson.com");

        when(userService.login(any(LoginRequest.class))).thenReturn(userDto);

        String viewName = userController.loginUser(request, redirectAttributes, session);

        assertEquals("redirect:/start", viewName);
        verify(session).setAttribute("user", userDto);
        verify(redirectAttributes).addFlashAttribute("success", "Välkommen " + userDto.name() + "!");
    }

    @Test
    void loginUser_withInvalidCredentials() {
        LoginRequest request = new LoginRequest("test@testsson.com", "wrongpassword");
        when(userService.login(any(LoginRequest.class))).thenReturn(null);

        String viewName = userController.loginUser(request, redirectAttributes, session);

        assertEquals("redirect:/login", viewName);
        verify(session, never()).setAttribute(anyString(), any());
        verify(redirectAttributes).addFlashAttribute("error", "Fel användarnamn eller lösenord");
    }
}