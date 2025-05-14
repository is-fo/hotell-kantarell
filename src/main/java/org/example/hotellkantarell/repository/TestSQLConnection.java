package org.example.hotellkantarell.repository;

import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.model.Room;
import org.example.hotellkantarell.model.User;
import org.example.hotellkantarell.util.DateUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TestSQLConnection implements CommandLineRunner {

    private final UserRepository userRepository;

    public TestSQLConnection(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        userRepository.deleteAll();
        userRepository.save(
                new User(
                        "Samme Eriksson",
                        "finalboss@samhall.se"
                )
        );
        User user = userRepository.findByEmail("finalboss@samhall.se");
        Room room = new Room(1, 2);
        Booking booking = new Booking(room, user, new Date(), DateUtil.nDaysInFuture(3));

    }
}
