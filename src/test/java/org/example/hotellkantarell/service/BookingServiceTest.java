package org.example.hotellkantarell.service;

import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.model.Room;
import org.example.hotellkantarell.repository.BookingRepository;
import org.example.hotellkantarell.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BookingRepository bookingRepository;

    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService(roomRepository, bookingRepository);
    }

    @Test
    void createBookingWhenRoomIsDoubleBooked() {
        Room room = new Room();
        room.setId(1L);

        Booking existingBooking = new Booking();
        existingBooking.setId(1L);
        existingBooking.setStartDate(date("2025-05-10"));
        existingBooking.setEndDate(date("2025-05-20"));
        existingBooking.setRoom(room);

        Booking newBooking = new Booking();
        newBooking.setId(2L);
        newBooking.setStartDate(date("2025-05-15"));
        newBooking.setEndDate(date("2025-05-25"));
        newBooking.setRoom(room);

        when(bookingRepository.findByRoomId(1L)).thenReturn(List.of(existingBooking));

        boolean result = bookingService.createBooking(newBooking);
        assertFalse(result);
    }

    @Test
    void findAvailableRoomsNoOverlap() {
        Room room = new Room();
        room.setId(1L);
        room.setBeds(2);
        room.setExtraBeds(1);

        Booking existingBooking = new Booking();
        existingBooking.setStartDate(date("2025-05-01"));
        existingBooking.setEndDate(date("2025-05-05"));
        existingBooking.setRoom(room);

        when(roomRepository.findAll()).thenReturn(List.of(room));
        when(bookingRepository.findByRoomId(1L)).thenReturn(List.of(existingBooking));

        List<Room> available = bookingService.findAvailableRooms(
                date("2025-05-10"),
                date("2025-05-15"),
                2
        );

        assertEquals(1, available.size());
        assertEquals(1L, available.getFirst().getId());
    }

    @Test
    void findAvailibleRoomsOverlap() {
        Room room = new Room();
        room.setId(2L);
        room.setBeds(2);
        room.setExtraBeds(0);

        Booking overlappingBooking = new Booking();
        overlappingBooking.setStartDate(date("2025-05-10"));
        overlappingBooking.setEndDate(date("2025-05-20"));
        overlappingBooking.setRoom(room);

        when(roomRepository.findAll()).thenReturn(List.of(room));
        when(bookingRepository.findByRoomId(2L)).thenReturn(List.of(overlappingBooking));

        List<Room> available = bookingService.findAvailableRooms(
                date("2025-05-15"),
                date("2025-05-25"),
                2
        );

        assertTrue(available.isEmpty());
    }

    private Date date(String yyyyMMdd) {
        return java.sql.Date.valueOf(yyyyMMdd);

    }
}