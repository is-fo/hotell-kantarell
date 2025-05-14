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

    public String register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()) != null) {
            return "Finns redan en användare med email: " + request.email();
        }
        userRepository.save(new User(
                request.name(),
                request.email(),
                passwordEncoder.encode(request.rawPassword())
        ));

        return "Registrerade användare: " + request.name();
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email());
        if (user == null || !passwordEncoder.matches(request.rawPassword(), user.getPasswordHash())) {
            return "Fel användarnamn eller lösenord";
        }

        return "Du är nu inloggad som: " + user.getName();
    }

}
