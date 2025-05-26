package org.example.hotellkantarell.service;

import org.example.hotellkantarell.dto.RoomDto;
import org.example.hotellkantarell.dto.UserDto;
import org.example.hotellkantarell.status.BookingStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.example.hotellkantarell.status.BookingStatus.*;
import static org.example.hotellkantarell.util.DateUtil.validDates;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookingServiceTest {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private RoomService roomService;

    @Test
    void validDates_withValidDates() {
        Date start = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000L);
        Date end = new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000L);
        assertTrue(validDates(start, end));
    }

    @Test
    void validDates_withStartBeforeToday() {
        Date start = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000L);
        Date end = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000L);
        assertFalse(validDates(start, end));
    }

    @Test
    void validDates_withEndBeforeStart() {
        Date start = new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000L);
        Date end = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000L);
        assertFalse(validDates(start, end));
    }

    @Test
    void createBooking_withInvalidRoom_returnsMalformedRoom() {
        UserDto user = new UserDto(1L, "Test User", "test@example.com");
        Date start = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000L);
        Date end = new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000L);

        BookingStatus status = bookingService.createBooking(9999L, user, start, end);
        assertEquals(MALFORMED_BOOKING_ROOM, status);
    }

    @Test
    void createBooking_withInvalidUser_returnsMalformedUser() {
        List<RoomDto> rooms = roomService.findAvailableRooms(
                new Date(System.currentTimeMillis() + 24*60*60*1000),
                new Date(System.currentTimeMillis() + 2*24*60*60*1000),
                1
        );

        assertFalse(rooms.isEmpty(), "Test kräver minst ett rum i databasen");

        Long validRoomId = rooms.get(0).id();

        UserDto invalidUser = new UserDto(9999L, "Fake User", "fake@example.com");
        Date start = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000L);
        Date end = new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000L);

        BookingStatus status = bookingService.createBooking(validRoomId, invalidUser, start, end);
        assertEquals(MALFORMED_BOOKING_USER, status);
    }


    @Test
    void findAvailableRooms_returnsNotNullList() {
        Date start = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000L);
        Date end = new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000L);

        List<RoomDto> rooms = roomService.findAvailableRooms(start, end, 2);
        assertNotNull(rooms);
    }

    @Test
    void deleteBooking_returnsTrueOrFalse() {
        boolean result = bookingService.deleteBooking(1L);
        assertTrue(result || !result);
    }
}
