package org.example.hotellkantarell.service;

import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.model.Room;
import org.example.hotellkantarell.model.User;
import org.example.hotellkantarell.repository.BookingRepository;
import org.example.hotellkantarell.repository.RoomRepository;
import org.springframework.stereotype.Service;

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

    public boolean createBooking(Booking booking) {
        if (
                booking.getStartDate().after(booking.getEndDate())
                || isRoomDoubleBooked(booking)) {
            return false;
        }

        bookingRepository.save(booking);
        return true;
    }

    public List<Room> findAvailableRooms(Date startDate, Date endDate, int guests) {
        List<Room> allRooms = roomRepository.findAll();

        return allRooms.stream()
                .filter(room -> {
                    int capacity = room.getBeds() + room.getExtraBeds();
                    return guests <= capacity;
                })
                .filter(room -> {
                    List<Booking> bookings = bookingRepository.findByRoomId(room.getId());
                    for (Booking booking : bookings) {
                        if (startDate.before(booking.getEndDate()) && endDate.after(booking.getStartDate())) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    public boolean updateBooking(Long id, Booking booking) {
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
                        booking.getStartDate().before(existing.getEndDate()) &&
                                booking.getEndDate().after(existing.getStartDate()));
    }

    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }
}
