package org.example.hotellkantarell.mapper;

import org.example.hotellkantarell.dto.BookingDto;
import org.example.hotellkantarell.dto.RoomDto;
import org.example.hotellkantarell.dto.UserDto;
import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.model.Room;
import org.example.hotellkantarell.model.User;
import org.springframework.stereotype.Service;

@Service
public class BookingMapper {

    private final RoomMapper roomMapper;
    private final UserMapper userMapper;

    public BookingMapper(RoomMapper roomMapper, UserMapper userMapper) {
        this.roomMapper = roomMapper;
        this.userMapper = userMapper;
    }

    public BookingDto bookingToDto(Booking booking) {
        RoomDto roomDto = roomMapper.roomToDto(booking.getRoom());
        UserDto userDto = userMapper.userToDto(booking.getUser());

        return new BookingDto(booking.getId(), roomDto, userDto, booking.getStartDate()
                , booking.getEndDate());
    }

    public Booking dtoToBooking(BookingDto bookingDto) {
        Room room = roomMapper.dtoToRoom(bookingDto.room());
        User user = userMapper.dtoToUser(bookingDto.user());

        return (bookingDto.id() != null)
                ? new Booking(bookingDto.id(), room, user, bookingDto.startDate(), bookingDto.endDate())
                : new Booking(room, user, bookingDto.startDate(), bookingDto.endDate());
    }

}
