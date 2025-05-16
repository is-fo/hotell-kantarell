package org.example.hotellkantarell.service;

import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.model.Room;
import org.example.hotellkantarell.repository.BookingRepository;
import org.example.hotellkantarell.repository.RoomRepository;
import org.example.hotellkantarell.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    private BookingRepository bookingRepository;
    private RoomRepository roomRepository;
    private UserRepository userRepository;
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingRepository = mock(BookingRepository.class);
        roomRepository = mock(RoomRepository.class);
        userRepository = mock(UserRepository.class);
        bookingService = new BookingService(userRepository, roomRepository, bookingRepository);
    }

    // Testar isRoomDoubleBooked indirekt via createBooking
    @Test
    void createBookingWhenRoomIsDoubleBooked() {
        Room room = new Room();
        room.setId(1L);

        Booking existingBooking = new Booking();
        existingBooking.setStartDate(date("2025-05-10"));
        existingBooking.setEndDate(date("2025-05-20"));
        existingBooking.setRoom(room);

        Booking newBooking = new Booking();
        newBooking.setStartDate(date("2025-05-15")); // överlappar existingBooking
        newBooking.setEndDate(date("2025-05-25"));
        newBooking.setRoom(room);

        when(bookingRepository.findByRoomId(1L)).thenReturn(List.of(existingBooking));

        String result = bookingService.createBooking(newBooking);

        assertEquals("Rummet är redan uppbokat under denna period", result);
    }

    // Testar findAvailableRooms
    @Test
    void findAvailableRooms() {
        Room room1 = new Room();
        room1.setId(1L);
        room1.setBeds(2);
        room1.setExtraBeds(0);

        Room room2 = new Room();
        room2.setId(2L);
        room2.setBeds(1);
        room2.setExtraBeds(1);

        Booking booking1 = new Booking();
        booking1.setStartDate(date("2025-06-05"));
        booking1.setEndDate(date("2025-06-10"));
        booking1.setRoom(room1);

        when(roomRepository.findAll()).thenReturn(List.of(room1, room2));
        when(bookingRepository.findByRoomId(1L)).thenReturn(List.of(booking1));
        when(bookingRepository.findByRoomId(2L)).thenReturn(Collections.emptyList());

        Date searchStart = date("2025-06-01");
        Date searchEnd = date("2025-06-04");
        int guests = 2;

        List<Room> availableRooms = bookingService.findAvailableRooms(searchStart, searchEnd, guests);

        // Båda rummen ska vara tillgängliga — inga bokningskonflikter och kapacitet ok
        assertTrue(availableRooms.contains(room1));
        assertTrue(availableRooms.contains(room2));
    }

    // Hjälpmetod för att skapa Date-objekt från yyyy-MM-dd
    private Date date(String yyyyMMdd) {
        Calendar cal = Calendar.getInstance();
        String[] parts = yyyyMMdd.split("-");
        cal.set(Calendar.YEAR, Integer.parseInt(parts[0]));
        cal.set(Calendar.MONTH, Integer.parseInt(parts[1]) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts[2]));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}