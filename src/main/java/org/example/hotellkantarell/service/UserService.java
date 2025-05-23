package org.example.hotellkantarell.service;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.hotellkantarell.dto.*;
import org.example.hotellkantarell.mapper.UserMapper;
import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.model.User;
import org.example.hotellkantarell.repository.BookingRepository;
import org.example.hotellkantarell.repository.UserRepository;
import org.example.hotellkantarell.status.RegisterStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, BookingRepository bookingRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public RegisterStatus register(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.email()) != null) {
            return RegisterStatus.EMAIL_IN_USE;
        }else if (registerRequest.name() == null || registerRequest.name().isEmpty()) {
            return RegisterStatus.MISSING_NAME;
        } else if (registerRequest.email() == null || registerRequest.email().isEmpty()) {
            return RegisterStatus.MISSING_EMAIL;
        } else if(registerRequest.rawPassword() == null || registerRequest.rawPassword().isEmpty()) {
            return RegisterStatus.MISSING_PASSWORD;
        } else{
        userRepository.save(new User(
                registerRequest.name(),
                registerRequest.email(),
                passwordEncoder.encode(registerRequest.rawPassword())));
        return RegisterStatus.SUCCESS;
        }
    }

    public UserDto login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email());
        if (user != null && passwordEncoder.matches(loginRequest.rawPassword(), user.getPasswordHash())) {
            return userMapper.userToDto(user);
        }
        return null;
    }


    public boolean deleteUser(UserDto userDto, HttpSession session) {

        Optional<User> exists = userRepository.findById(userDto.id());
        List<Booking> bookings = bookingRepository.findByUserId(userDto.id());
        if (exists.isEmpty() || bookings == null || !bookings.isEmpty()) {
            return false;
        }

        userRepository.delete(exists.get());
        session.invalidate();
        return true;
    }

    public UserDto editProfile(UserDto userDto, @Valid @ModelAttribute EditProfileRequest request) {
        User user = userRepository.findById(userDto.id()).get();
        if (request.name() != null) {
            user.setName(request.name());
        }

        if (request.email() != null) {
            user.setEmail(request.email());
        }

        return userMapper.userToDto(userRepository.save(user));
    }

    public boolean editPassword(UserDto userDto, @ModelAttribute EditPasswordRequest request, RedirectAttributes redirectAttributes, HttpSession session) {
        User user = userMapper.dtoToUser(userDto);
        if (request.rawPassword() != null && request.rawPassword().length() > 5) {
            user.setPasswordHash(passwordEncoder.encode(request.rawPassword()));
            session.setAttribute("user", userMapper.userToDto(userRepository.save(user)));
            redirectAttributes.addFlashAttribute("success", "Ändrade lösenordet!");
            return true;
        } else {
            redirectAttributes.addFlashAttribute("error", "Lösenordet måste vara minst 6 karaktärer.");
            return false;
        }
    }

}
