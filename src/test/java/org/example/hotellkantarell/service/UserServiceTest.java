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
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
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
    void register_withExistingEmail_returnsEmailInUse() {
        RegisterRequest request = new RegisterRequest("Test User", "duplicate@example.com", "password123");
        when(userRepository.findByEmail("duplicate@example.com")).thenReturn(new User());

        RegisterStatus status = userService.register(request);

        assertEquals(RegisterStatus.EMAIL_IN_USE, status);
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_withMissingName_returnsMissingName() {
        RegisterRequest request = new RegisterRequest("", "test@example.com", "password123");
        RegisterStatus status = userService.register(request);
        assertEquals(RegisterStatus.MISSING_NAME, status);
    }

    @Test
    void register_withMissingEmail_returnsMissingEmail() {
        RegisterRequest request = new RegisterRequest("Test User", "", "password123");
        RegisterStatus status = userService.register(request);
        assertEquals(RegisterStatus.MISSING_EMAIL, status);
    }

    @Test
    void register_withMissingPassword_returnsMissingPassword() {
        RegisterRequest request = new RegisterRequest("Test User", "test@example.com", "");
        RegisterStatus status = userService.register(request);
        assertEquals(RegisterStatus.MISSING_PASSWORD, status);
    }

    @Test
    void register_withValidData_returnsSuccess() {
        RegisterRequest request = new RegisterRequest("Test User", "unique@example.com", "password123");
        when(userRepository.findByEmail("unique@example.com")).thenReturn(null);
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        RegisterStatus status = userService.register(request);

        assertEquals(RegisterStatus.SUCCESS, status);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void login_withValidCredentials_returnsUserDto() {
        LoginRequest loginRequest = new LoginRequest("user@example.com", "password123");
        User user = new User();
        user.setEmail("user@example.com");
        user.setPasswordHash("hashedPassword");

        when(userRepository.findByEmail("user@example.com")).thenReturn(user);
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
        when(userMapper.userToDto(user)).thenReturn(new UserDto(1L, "Test User", "user@example.com"));

        UserDto userDto = userService.login(loginRequest);

        assertNotNull(userDto);
        assertEquals("user@example.com", userDto.email());
    }

    @Test
    void login_withInvalidPassword_returnsNull() {
        LoginRequest loginRequest = new LoginRequest("user@example.com", "wrongpassword");
        User user = new User();
        user.setPasswordHash("hashedPassword");

        when(userRepository.findByEmail("user@example.com")).thenReturn(user);
        when(passwordEncoder.matches("wrongpassword", "hashedPassword")).thenReturn(false);

        UserDto userDto = userService.login(loginRequest);

        assertNull(userDto);
    }

    @Test
    void deleteUser_withNoBookings_returnsTrue_andInvalidatesSession() {
        UserDto userDto = new UserDto(1L, "Test User", "user@example.com");
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
    void deleteUser_withBookings_returnsFalse() {
        UserDto userDto = new UserDto(1L, "Test User", "user@example.com");
        User user = new User();

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(bookingRepository.findByUserId(1L)).thenReturn(java.util.List.of(new Booking()));

        boolean result = userService.deleteUser(userDto, mock(HttpSession.class));

        assertFalse(result);
        verify(userRepository, never()).delete(any());
    }
}
