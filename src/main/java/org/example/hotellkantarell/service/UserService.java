package org.example.hotellkantarell.service;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.example.hotellkantarell.dto.LoginRequest;
import org.example.hotellkantarell.dto.RegisterRequest;
import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.model.User;
import org.example.hotellkantarell.repository.BookingRepository;
import org.example.hotellkantarell.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    Validator validator;

    public UserService(UserRepository userRepository, BookingRepository bookingRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.email()) != null) {
            return null;
        }

        return userRepository.save(new User(
                registerRequest.name(),
                registerRequest.email(),
                passwordEncoder.encode(registerRequest.rawPassword())
        ));
    }

    public User login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email());
        if (user != null && passwordEncoder.matches(loginRequest.rawPassword(), user.getPasswordHash())) {
            return user;
        }
        return null;
    }


    public boolean deleteUser(User user) {
        Optional<User> exists = userRepository.findById(user.getId());
        List<Booking> bookings = bookingRepository.findByUserId(user.getId());
        if (exists.isEmpty() || bookings == null || !bookings.isEmpty()) {
            return false;
        }

        userRepository.delete(exists.get());
        return true;
    }

    public User editProfile(User user, @Valid RegisterRequest request) {
        if (request.name() != null) {
            user.setName(request.name());
        }

        if (request.email() != null) {
            user.setEmail(request.email());
        }
        if (request.rawPassword() != null && !request.rawPassword().isBlank()) {
            String hashedPassword = passwordEncoder.encode(request.rawPassword());
            user.setPasswordHash(hashedPassword);
        }

        return userRepository.save(user);
    }

}
