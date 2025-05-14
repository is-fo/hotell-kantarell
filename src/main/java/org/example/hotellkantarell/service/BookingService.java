package org.example.hotellkantarell.service;

import org.example.hotellkantarell.repository.BookingRepository;
import org.example.hotellkantarell.repository.RoomRepository;
import org.example.hotellkantarell.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    final
    UserRepository userRepository;
    final
    RoomRepository roomRepository;
    final
    BookingRepository bookingRepository;

    public BookingService(UserRepository userRepository, RoomRepository roomRepository, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

}
