package org.example.hotellkantarell.service;

import org.example.hotellkantarell.dto.LoginRequest;
import org.example.hotellkantarell.dto.RegisterRequest;
import org.example.hotellkantarell.model.User;
import org.example.hotellkantarell.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean register(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.email()) != null) {
            return false;
        }
        userRepository.save(new User(
                registerRequest.name(),
                registerRequest.email(),
                passwordEncoder.encode(registerRequest.rawPassword())
        ));

        return true;
    }

    public boolean login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email());
        return user != null && passwordEncoder.matches(loginRequest.rawPassword(), user.getPasswordHash());
    }

}
