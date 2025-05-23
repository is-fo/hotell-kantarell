package org.example.hotellkantarell.mapper;

import org.example.hotellkantarell.dto.*;
import org.example.hotellkantarell.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookingMapperTest {

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    void bookingToDto_convertsCorrectly() {
        Room room = new Room(1L, 2, 1, 25, "image.jpg", 1000, "101");
        User user = new User(1L, "Test", "test@test.com", "hash");
        Booking booking = new Booking(1L, room, user, new Date(), new Date());

        BookingDto dto = bookingMapper.bookingToDto(booking);

        assertEquals(booking.getId(), dto.id());
        assertEquals(room.getId(), dto.room().id());
        assertEquals(user.getId(), dto.user().id());
    }

    @Test
    void dtoToBooking_convertsCorrectly() {
        RoomDto roomDto = new RoomDto(1L, 2, 1, 25, "image.jpg", 1000, "101");
        UserDto userDto = new UserDto(1L, "Test", "test@test.com");
        BookingDto bookingDto = new BookingDto(1L, roomDto, userDto, new Date(), new Date());

        Booking booking = bookingMapper.dtoToBooking(bookingDto);

        assertEquals(bookingDto.id(), booking.getId());
        assertEquals(bookingDto.room().id(), booking.getRoom().getId());
        assertEquals(bookingDto.user().id(), booking.getUser().getId());
    }
}