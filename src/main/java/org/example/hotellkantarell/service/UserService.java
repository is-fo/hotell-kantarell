package org.example.hotellkantarell.service;

import jakarta.validation.Valid;
import org.example.hotellkantarell.dto.*;
import org.example.hotellkantarell.mapper.UserMapper;
import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.model.User;
import org.example.hotellkantarell.repository.BookingRepository;
import org.example.hotellkantarell.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

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

    public UserDto register(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.email()) != null) {
            return null;
        }

        return userMapper.userToDto(userRepository.save(new User(
                registerRequest.name(),
                registerRequest.email(),
                passwordEncoder.encode(registerRequest.rawPassword())
        )));
    }

    public UserDto login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email());
        if (user != null && passwordEncoder.matches(loginRequest.rawPassword(), user.getPasswordHash())) {
            return userMapper.userToDto(user);
        }
        return null;
    }


    public boolean deleteUser(UserDto userDto) {

        Optional<User> exists = userRepository.findById(userDto.id());
        List<Booking> bookings = bookingRepository.findByUserId(userDto.id());
        if (exists.isEmpty() || bookings == null || !bookings.isEmpty()) {
            return false;
        }

        userRepository.delete(exists.get());
        return true;
    }

    public UserDto editProfile(UserDto userDto, @Valid @ModelAttribute EditProfileRequest request) {
        User user = userMapper.dtoToUser(userDto);
        if (request.name() != null) {
            user.setName(request.name());
        }

        if (request.email() != null) {
            user.setEmail(request.email());
        }

        return userMapper.userToDto(userRepository.save(user));
    }

    public UserDto editPassword(UserDto userDto, @Valid @ModelAttribute EditPasswordRequest request) {
        User user = userMapper.dtoToUser(userDto);
        if (request.rawPassword() != null) {
            user.setPasswordHash(passwordEncoder.encode(request.rawPassword()));
        }
        return userMapper.userToDto(userRepository.save(user));
    }

}
