package org.example.hotellkantarell.service;

import jakarta.servlet.http.HttpSession;
import org.example.hotellkantarell.dto.*;
import org.example.hotellkantarell.mapper.UserMapper;
import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.model.User;
import org.example.hotellkantarell.repository.BookingRepository;
import org.example.hotellkantarell.repository.UserRepository;
import org.example.hotellkantarell.status.RegisterStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class UserServiceTest {

    private UserService userService;

    private UserRepository userRepository = mock(UserRepository.class);
    private BookingRepository bookingRepository = mock(BookingRepository.class);
    private PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private UserMapper userMapper = mock(UserMapper.class);

    @BeforeEach
    void setup() {
        userService = new UserService(userRepository, bookingRepository, passwordEncoder, userMapper);
    }

    @Test
    void register_withExistingEmail() {
        RegisterRequest request = new RegisterRequest("Duplicate Testsson", "duplicate@testsson.com", "password123");
        when(userRepository.findByEmail("duplicate@testsson.com")).thenReturn(new User());

        RegisterStatus status = userService.register(request);

        assertEquals(RegisterStatus.EMAIL_IN_USE, status);
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_withMissingName() {
        RegisterRequest request = new RegisterRequest("", "test@testsson.com", "password123");
        RegisterStatus status = userService.register(request);
        assertEquals(RegisterStatus.MISSING_NAME, status);
    }

    @Test
    void register_withMissingEmail() {
        RegisterRequest request = new RegisterRequest("Test Testsson", "", "password123");
        RegisterStatus status = userService.register(request);
        assertEquals(RegisterStatus.MISSING_EMAIL, status);
    }

    @Test
    void register_withMissingPassword() {
        RegisterRequest request = new RegisterRequest("Test User", "test@testsson.com", "");
        RegisterStatus status = userService.register(request);
        assertEquals(RegisterStatus.MISSING_PASSWORD, status);
    }

    @Test
    void register_withValidData() {
        RegisterRequest request = new RegisterRequest("Unique Testsson", "unique@testsson.com", "password123");
        when(userRepository.findByEmail("unique@testsson.com")).thenReturn(null);
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        RegisterStatus status = userService.register(request);

        assertEquals(RegisterStatus.SUCCESS, status);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void login_withValidCredentials() {
        LoginRequest loginRequest = new LoginRequest("test@testsson.com", "password123");
        User user = new User();
        user.setEmail("test@testsson.com");
        user.setPasswordHash("hashedPassword");

        when(userRepository.findByEmail("test@testsson.com")).thenReturn(user);
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
        when(userMapper.userToDto(user)).thenReturn(new UserDto(1L, "Test Testsson", "test@testsson.com"));

        UserDto userDto = userService.login(loginRequest);

        assertNotNull(userDto);
        assertEquals("test@testsson.com", userDto.email());
    }

    @Test
    void login_withInvalidPassword() {
        LoginRequest loginRequest = new LoginRequest("test@testsson.com", "wrongpassword");
        User user = new User();
        user.setPasswordHash("hashedPassword");

        when(userRepository.findByEmail("test@testsson.com")).thenReturn(user);
        when(passwordEncoder.matches("wrongpassword", "hashedPassword")).thenReturn(false);

        UserDto userDto = userService.login(loginRequest);

        assertNull(userDto);
    }

    @Test
    void deleteUser_withNoBookings() {
        UserDto userDto = new UserDto(1L, "Test Testsson", "test@testsson.com");
        User user = new User();
        user.setId(1L);

        HttpSession session = mock(HttpSession.class);

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(bookingRepository.findByUserId(1L)).thenReturn(java.util.Collections.emptyList());

        boolean result = userService.deleteUser(userDto, session);

        assertTrue(result);
        verify(userRepository).delete(user);
        verify(session).invalidate();
    }

    @Test
    void deleteUser_withBookings() {
        UserDto userDto = new UserDto(1L, "Test Testsson", "test@testsson.com");
        User user = new User();

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(bookingRepository.findByUserId(1L)).thenReturn(java.util.List.of(new Booking()));

        boolean result = userService.deleteUser(userDto, mock(HttpSession.class));

        assertFalse(result);
        verify(userRepository, never()).delete(any());
    }
}
