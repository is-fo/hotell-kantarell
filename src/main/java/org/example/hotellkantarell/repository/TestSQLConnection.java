package org.example.hotellkantarell.repository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestSQLConnection implements CommandLineRunner {

    final UserRepository userRepository;
    final BookingRepository bookingRepository;
    final RoomRepository roomRepository;

    public TestSQLConnection(UserRepository userRepository, BookingRepository bookingRepository, RoomRepository roomRepository) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        bookingRepository.deleteAll();
//        userRepository.deleteAll();
//        roomRepository.deleteAll();
    }
}
