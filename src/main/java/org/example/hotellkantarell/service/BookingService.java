package org.example.hotellkantarell.service;

import org.example.hotellkantarell.dto.BookingDto;
import org.example.hotellkantarell.dto.RoomDto;
import org.example.hotellkantarell.dto.UserDto;
import org.example.hotellkantarell.mapper.BookingMapper;
import org.example.hotellkantarell.mapper.RoomMapper;
import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.repository.BookingRepository;
import org.example.hotellkantarell.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;



@Service
public class BookingService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final RoomMapper roomMapper;

    public BookingService(RoomRepository roomRepository, BookingRepository bookingRepository, BookingMapper bookingMapper, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.roomMapper = roomMapper;
    }

    public List<BookingDto> findBookingByUser(UserDto user) {
        List<BookingDto> bookings = new ArrayList<>();
        bookingRepository.findByUserId(user.id()).forEach(
                e -> bookings.add(bookingMapper.bookingToDto(e))
        );
        return bookings;
    }

    private Date setTime(Date date, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public boolean createBooking(BookingDto booking) {
        Booking newBooking = bookingMapper.dtoToBooking(booking);
        newBooking.setStartDate(setTime(booking.startDate(), 16));
        newBooking.setEndDate(setTime(booking.endDate(), 12));
        Date now = new Date();
        if (newBooking.getStartDate().before(now)) {
            return false;
        }

        if (newBooking.getStartDate().after(booking.endDate()) || isRoomDoubleBooked(booking)) {
            return false;
        }


        bookingRepository.save(bookingMapper.dtoToBooking(booking));
        return true;
    }

    public List<RoomDto> findAvailableRooms(Date startDate, Date endDate, int guests) {
        Date start = setTime(startDate, 16);
        Date end = setTime(endDate, 12);

        List<RoomDto> allRooms = roomRepository.findAll().stream().map(roomMapper::roomToDto).toList();

        return allRooms.stream()
                .filter(room -> {
                    int capacity = room.beds() + room.extraBeds();
                    return guests <= capacity;
                })
                .filter(room -> {
                    List<Booking> bookings = bookingRepository.findByRoomId(room.id());
                    for (Booking booking : bookings) {
                        if (!(end.compareTo(booking.getStartDate()) <= 0 || start.compareTo(booking.getEndDate()) >= 0)) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    public boolean updateBooking(Long id, BookingDto booking) {
//        booking.setStartDate(setTime(booking.getStartDate(), 16));
//        booking.setEndDate(setTime(booking.getEndDate(), 12));

        if (booking.startDate().before(new Date())) {
            return false;
        }

        Booking existing = bookingRepository.findById(id).orElse(null);
        if (
                existing == null ||
                booking.startDate().after(booking.endDate()) ||
                isRoomDoubleBooked(booking)) {
            return false;
        }

        Booking updatedBooking = bookingMapper.dtoToBooking(booking);
        updatedBooking.setId(id);
        bookingRepository.save(updatedBooking);
        return true;
    }

    public boolean deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            return false;
        }

        bookingRepository.deleteById(id);
        return true;
    }

    private boolean isRoomDoubleBooked(BookingDto booking) {
        return bookingRepository.findByRoomId(booking.room().id())
                .stream()
                .filter(existing -> !existing.getId().equals(booking.id()))
                .anyMatch(existing ->
                        !(booking.endDate().compareTo(existing.getStartDate()) <= 0 ||
                                booking.startDate().compareTo(existing.getEndDate()) >= 0)
                );
    }

    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }
}
