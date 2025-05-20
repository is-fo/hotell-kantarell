package org.example.hotellkantarell.service;

import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.model.Room;
import org.example.hotellkantarell.model.User;
import org.example.hotellkantarell.repository.BookingRepository;
import org.example.hotellkantarell.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
public class BookingService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    public BookingService(RoomRepository roomRepository, BookingRepository bookingRepository) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> findBookingByUser(User user) {
        return bookingRepository.findByUserId(user.getId());
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

    public boolean createBooking(Booking booking) {
        booking.setStartDate(setTime(booking.getStartDate(), 16));
        booking.setEndDate(setTime(booking.getEndDate(), 12));

        Date now = new Date();
        if (booking.getStartDate().before(now)) {
            return false;
        }

        if (booking.getStartDate().after(booking.getEndDate()) || isRoomDoubleBooked(booking)) {
            return false;
        }

        bookingRepository.save(booking);
        return true;
    }

    public List<Room> findAvailableRooms(Date startDate, Date endDate, int guests) {
        Date start = setTime(startDate, 16);
        Date end = setTime(endDate, 12);

        List<Room> allRooms = roomRepository.findAll();

        return allRooms.stream()
                .filter(room -> {
                    int capacity = room.getBeds() + room.getExtraBeds();
                    return guests <= capacity;
                })
                .filter(room -> {
                    List<Booking> bookings = bookingRepository.findByRoomId(room.getId());
                    for (Booking booking : bookings) {
                        if (!(end.compareTo(booking.getStartDate()) <= 0 || start.compareTo(booking.getEndDate()) >= 0)) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    public boolean updateBooking(Long id, Booking booking) {
        booking.setStartDate(setTime(booking.getStartDate(), 16));
        booking.setEndDate(setTime(booking.getEndDate(), 12));

        Date now = new Date();
        if (booking.getStartDate().before(now)) {
            return false;
        }

        Booking existing = bookingRepository.findById(id).orElse(null);
        if (
                existing == null ||
                booking.getStartDate().after(booking.getEndDate()) ||
                isRoomDoubleBooked(booking)) {
            return false;
        }

        booking.setId(id);
        bookingRepository.save(booking);
        return true;
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

    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }
}
