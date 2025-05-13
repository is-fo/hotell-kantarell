package org.example.hotellkantarell.repository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestSQLConnection implements CommandLineRunner {

    private final UserRepository userRepository;

    public TestSQLConnection(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println(userRepository.findAll());
//        userRepository.save(
//                new User(
//                        "Samme Eriksson",
//                        "finalboss@samhall.se"
//                )
//        );
    }
}
