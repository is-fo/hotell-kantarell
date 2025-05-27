package org.example.hotellkantarell.service;

import org.example.hotellkantarell.dto.BookingDto;
import org.example.hotellkantarell.dto.RoomDto;
import org.example.hotellkantarell.dto.UserDto;
import org.example.hotellkantarell.mapper.BookingMapper;
import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.model.Room;
import org.example.hotellkantarell.model.User;
import org.example.hotellkantarell.repository.BookingRepository;
import org.example.hotellkantarell.repository.RoomRepository;
import org.example.hotellkantarell.repository.UserRepository;
import org.example.hotellkantarell.status.BookingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Method;
import java.util.*;

import static org.example.hotellkantarell.status.BookingStatus.*;
import static org.example.hotellkantarell.util.DateUtil.validDates;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookingServiceTest {

    private BookingRepository bookingRepository;
    private BookingMapper bookingMapper;
    private RoomRepository roomRepository;
    private UserRepository userRepository;
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingRepository = mock(BookingRepository.class);
        bookingMapper = mock(BookingMapper.class);
        roomRepository = mock(RoomRepository.class);
        userRepository = mock(UserRepository.class);
        bookingService = new BookingService(roomRepository, bookingRepository, bookingMapper, userRepository);
    }

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
    void createBooking_whenValidInput() {
        Long roomId = 1L;
        UserDto userDto = new UserDto(1L, "Test Testsson", "test@testsson.com");
        Room room = new Room(); room.setId(roomId);
        User user = new User(); user.setId(1L);
        Date start = tomorrowAtHour(10);
        Date end = dayAfterTomorrowAtHour(10);

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(userRepository.findById(userDto.id())).thenReturn(Optional.of(user));
        when(bookingRepository.findByRoomId(roomId)).thenReturn(Collections.emptyList());

        BookingStatus result = bookingService.createBooking(roomId, userDto, start, end);

        assertEquals(BookingStatus.SUCCESS, result);
        verify(bookingRepository).save(any());
    }

    @Test
    void createBooking_whenRoomNotFound() {
        Long roomId = 1L;
        UserDto userDto = new UserDto(1L, "Test Testsson", "test@testsson.com");
        Date start = tomorrowAtHour(10);
        Date end = dayAfterTomorrowAtHour(10);

        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        BookingStatus result = bookingService.createBooking(roomId, userDto, start, end);

        assertEquals(BookingStatus.MALFORMED_BOOKING_ROOM, result);
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void updateBooking_whenValidInput() {
        Long bookingId = 1L;
        UserDto userDto = new UserDto(1L, "Test Testsson", "test@testsson.com");
        User user = new User(); user.setId(1L);
        Booking booking = new Booking(); booking.setUser(user); booking.setId(bookingId);
        booking.setRoom(new Room()); booking.getRoom().setId(1L);

        Date start = tomorrowAtHour(10);
        Date end = dayAfterTomorrowAtHour(10);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userRepository.findById(userDto.id())).thenReturn(Optional.of(user));
        when(bookingRepository.findByRoomId(any())).thenReturn(Collections.emptyList());

        BookingStatus result = bookingService.updateBooking(userDto, bookingId, start, end);

        assertEquals(BookingStatus.SUCCESS, result);
        verify(bookingRepository).save(booking);
    }

    @Test
    void updateBooking_whenStartAfterEnd() {
        Long bookingId = 1L;
        UserDto userDto = new UserDto(1L, "Test Testsson", "test@testsson.com");
        User user = new User(); user.setId(1L);
        Booking booking = new Booking(); booking.setUser(user); booking.setId(bookingId);
        booking.setRoom(new Room()); booking.getRoom().setId(1L);

        Date start = dayAfterTomorrowAtHour(10);
        Date end = tomorrowAtHour(10);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userRepository.findById(userDto.id())).thenReturn(Optional.of(user));

        BookingStatus result = bookingService.updateBooking(userDto, bookingId, start, end);

        assertEquals(BookingStatus.REVERSE_DATE, result);
        verify(bookingRepository, never()).save(any());
    }


    @Test
    void deleteBooking_shouldReturnTrue() {
        Long bookingId = 1L;
        UserDto user = new UserDto(2L, "Test Testsson", "test@testsson.com");
        BookingDto bookingDto = new BookingDto(
                bookingId,
                new RoomDto(1L, 2, 0, 20, "img.jpg", 1000, "101"),
                user,
                new Date(),
                new Date()
        );


        when(bookingRepository.findById(bookingId)).thenReturn(java.util.Optional.of(mock(org.example.hotellkantarell.model.Booking.class)));
        when(bookingMapper.bookingToDto(any())).thenReturn(bookingDto);
        when(bookingRepository.existsById(bookingId)).thenReturn(true);

        boolean result = bookingService.deleteBooking(bookingId, user);

        assertTrue(result);
        verify(bookingRepository).deleteById(bookingId);
    }

    @Test
    void deleteBooking_shouldReturnFalse() {
        Long bookingId = 1L;
        UserDto user = new UserDto(2L, "Test Testsson", "test@testsson.com");

        when(bookingRepository.findById(bookingId)).thenReturn(java.util.Optional.empty());

        boolean result = bookingService.deleteBooking(bookingId, user);

        assertFalse(result);
        verify(bookingRepository, never()).deleteById(any());
    }

    @Test
    void testIsRoomDoubleBooked_ShouldReturnTrue() throws Exception {
        Room room = new Room();
        room.setId(1L);
        User user = new User();
        user.setId(1L);

        Date start = new Date();
        Date end = new Date(start.getTime() + 2 * 24 * 60 * 60 * 1000);

        Booking newBooking = new Booking(10L, room, user, start, end);


        Booking existing = new Booking(11L, room, user,
                new Date(start.getTime() + 12 * 60 * 60 * 1000),
                new Date(end.getTime() + 1 * 24 * 60 * 60 * 1000));

        when(bookingRepository.findByRoomId(1L)).thenReturn(List.of(existing));

        Method method = BookingService.class.getDeclaredMethod("isRoomDoubleBooked", Booking.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(bookingService, newBooking);

        assertTrue(result);
    }

    @Test
    void testIsRoomDoubleBooked_ShouldReturnFalse() throws Exception {
        Room room = new Room();
        room.setId(1L);
        User user = new User();
        user.setId(1L);

        Date start = new Date();
        Date end = new Date(start.getTime() + 1 * 24 * 60 * 60 * 1000);

        Booking newBooking = new Booking(10L, room, user, start, end);


        Booking existing = new Booking(11L, room, user,
                new Date(end.getTime() + 1 * 24 * 60 * 60 * 1000),
                new Date(end.getTime() + 2 * 24 * 60 * 60 * 1000));

        when(bookingRepository.findByRoomId(1L)).thenReturn(List.of(existing));

        Method method = BookingService.class.getDeclaredMethod("isRoomDoubleBooked", Booking.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(bookingService, newBooking);

        assertFalse(result);
    }


    @Test
    void testFindById_ReturnsBookingDto() {

        Long bookingId = 1L;

        Room room = new Room();
        room.setId(1L);
        User user = new User();
        user.setId(1L);

        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 86400000);

        Booking booking = new Booking(bookingId, room, user, startDate, endDate);
        BookingDto bookingDto = new BookingDto(
                bookingId,
                new RoomDto(1L, 2, 1, 25, "img.jpg", 1000, "101"),
                new UserDto(1L, "Test testsson", "test@testsson.com"),
                startDate,
                endDate
        );

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingMapper.bookingToDto(booking)).thenReturn(bookingDto);

        BookingDto result = bookingService.findById(bookingId);

        assertNotNull(result);
        assertEquals(bookingDto, result);
    }

    @Test
    void testFindById_ReturnsNull() {

        Long bookingId = 99L;
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        BookingDto result = bookingService.findById(bookingId);

        assertNull(result);
    }

    private Date tomorrowAtHour(int hour) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    private Date dayAfterTomorrowAtHour(int hour) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 2);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }
}
