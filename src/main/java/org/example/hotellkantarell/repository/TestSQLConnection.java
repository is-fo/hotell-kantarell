package org.example.hotellkantarell.repository;

import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.model.Room;
import org.example.hotellkantarell.model.User;
import org.example.hotellkantarell.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TestSQLConnection implements CommandLineRunner {

    private final UserRepository userRepository;
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
