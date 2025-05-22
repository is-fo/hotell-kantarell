package org.example.hotellkantarell.service;

import org.example.hotellkantarell.dto.LoginRequest;
import org.example.hotellkantarell.dto.RegisterRequest;
import org.example.hotellkantarell.dto.UserDto;
import org.example.hotellkantarell.mapper.UserMapper;
import org.example.hotellkantarell.model.User;
import org.example.hotellkantarell.repository.BookingRepository;
import org.example.hotellkantarell.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, bookingRepository, passwordEncoder, userMapper);
    }

    @Test
    void registerUserThatAlreadyExists() {
        RegisterRequest request = new RegisterRequest("John Doe", "johndoe@gmail.com", "secretpassword");
        when(userRepository.findByEmail("johndoe@gmail.com")).thenReturn(null);

        User result = userService.register(request);

        assertNull(result);
    }

    @Test
    void registerNewUserSuccessfully() {
        RegisterRequest request = new RegisterRequest("Jane Doe", "janedoe@gmail.com", "secretpassword");
        when(userRepository.findByEmail("janedoe@gmail.com")).thenReturn(null);
        when(passwordEncoder.encode("secretpassword")).thenReturn("hashedpassword");
        User savedUser = new User("Jane Doe", "janedoe@gmail.com", "hashedpassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.register(request);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        assertEquals("janedoe@gmail.com", result.getEmail());
        assertEquals("hashedpassword", result.getPasswordHash());
    }

    @Test
    void loginWithCorrectCredentials() {
        LoginRequest loginRequest = new LoginRequest("test-testsson@hotmail.com", "correctpassword");

        User user = new User("Test Testsson", "test-testsson@hotmail.com", "hashedpassword");

        when(userRepository.findByEmail("test-testsson@hotmail.com")).thenReturn(user);
        when(passwordEncoder.matches("correctpassword", "hashedpassword")).thenReturn(true);

        UserDto result = userService.login(loginRequest);

        assertNotNull(result);
        assertEquals("test-testsson@hotmail.com", result.email());
    }

    @Test
    void loginWithWrongPassword() {
        LoginRequest loginRequest = new LoginRequest("batman@outlook.com", "wrongpassword");
        User user = new User("Bruce Wayne", "batman@outlook.com", "hashedpassword");

        when(userRepository.findByEmail("batman@outlook.com")).thenReturn(user);
        when(passwordEncoder.matches("wrongpassword", "hashedpassword")).thenReturn(false);

        UserDto result = userService.login(loginRequest);

        assertNull(result);
    }

//    @Test
//    void deleteUserWithExistingBookings() {
//        UserDto user = new UserDto(
//                1L,
//                "Test Testsson",
//                "test@teststtts.com"
//        );
//
//        when(userRepository.findById(1L)).thenReturn(user);
//        when(bookingRepository.findByUserId(1L)).thenReturn(List.of(new Booking()));
//
//        boolean result = userService.deleteUser(user);
//
//        assertFalse(result);
//    }

//    @Test
//    void deleteUserSuccessfully() {
//        User user = new User();
//        user.setId(1L);
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        when(bookingRepository.findByUserId(1L)).thenReturn(List.of());
//
//        boolean result = userService.deleteUser(user);
//
//        assertTrue(result);
//        verify(userRepository).delete(user);
//    }

}