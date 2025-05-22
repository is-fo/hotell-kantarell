package org.example.hotellkantarell.service;

import org.example.hotellkantarell.dto.BookingDto;
import org.example.hotellkantarell.dto.RoomDto;
import org.example.hotellkantarell.dto.UserDto;
import org.example.hotellkantarell.status.BookingStatus;
import org.example.hotellkantarell.mapper.BookingMapper;
import org.example.hotellkantarell.mapper.RoomMapper;
import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.repository.BookingRepository;
import org.example.hotellkantarell.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static org.example.hotellkantarell.status.BookingStatus.*;

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

        if (newBooking.getStartDate().after(booking.endDate()) || isRoomDoubleBooked(newBooking)) {
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

    public BookingStatus updateBooking(UserDto user, Long id, Date start, Date end) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (start.before(new Date())) {
            return EXPIRED_DATE;
        }
        if (start.after(end)) {
            return REVERSE_DATE;
        }
        if (booking == null) {
            return NO_SUCH_BOOKING;
        }
        if (user == null) {
            return MALFORMED_BOOKING_USER;
        }
        if (!Objects.equals(booking.getUser().getId(), user.id())) {
            return ILLEGAL_ACCESS;
        }
        if (isRoomDoubleBooked(booking)) {
            return DOUBLE_BOOKED;
        }
        booking.setStartDate(setTime(start, 16));
        booking.setEndDate(setTime(end, 12));

        bookingRepository.save(booking);
        return SUCCESS;
    }

    public boolean deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            return false;
        }

        bookingRepository.deleteById(id);
        return true;
    }

    private boolean isRoomDoubleBooked(Booking booking) {
        return bookingRepository.findByRoomId(booking.getRoom().getId())
                .stream()
                .filter(existing -> !existing.getId().equals(booking.getId()))
                .anyMatch(existing ->
                        !(booking.getEndDate().compareTo(existing.getStartDate()) <= 0 ||
                                booking.getStartDate().compareTo(existing.getEndDate()) >= 0)
                );
    }

    public BookingDto findById(Long id) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        return booking != null ? bookingMapper.bookingToDto(booking) : null;
    }
}
