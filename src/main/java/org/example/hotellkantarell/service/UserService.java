package org.example.hotellkantarell.service;

import org.example.hotellkantarell.dto.LoginRequest;
import org.example.hotellkantarell.dto.RegisterRequest;
import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.model.User;
import org.example.hotellkantarell.repository.BookingRepository;
import org.example.hotellkantarell.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final PasswordEncoder passwordEncoder;

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

}
