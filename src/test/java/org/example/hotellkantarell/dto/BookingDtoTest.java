package org.example.hotellkantarell.dto;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class BookingDtoTest {

    @Test
    void totalPrice_forOneNight() {
        RoomDto room = new RoomDto(1L, 2, 1, 25, "image.jpg", 1000, "101");
        UserDto user = new UserDto(1L, "Test", "test@test.com");

        Date start = new Date();
        Date end = new Date(start.getTime() + 86400000);

        BookingDto booking = new BookingDto(1L, room, user, start, end);

        assertEquals(1000L, booking.totalPrice());
    }

    @Test
    void totalPrice_forMultipleNights() {
        RoomDto room = new RoomDto(1L, 2, 1, 25, "image.jpg", 1000, "101");
        UserDto user = new UserDto(1L, "Test", "test@test.com");

        Date start = new Date();
        Date end = new Date(start.getTime() + 86400000 * 5);

        BookingDto booking = new BookingDto(1L, room, user, start, end);

        assertEquals(5000L, booking.totalPrice());
    }
}