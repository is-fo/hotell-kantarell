package org.example.hotellkantarell.service;

import jakarta.servlet.http.HttpServletResponse;
import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.model.Room;
import org.example.hotellkantarell.repository.BookingRepository;
import org.example.hotellkantarell.repository.RoomRepository;
import org.example.hotellkantarell.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    public BookingService(UserRepository userRepository, RoomRepository roomRepository, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

    public String createBooking(Booking booking) {
        if (booking.getStartDate().after(booking.getEndDate())) {
            return "Startdatumet måste vara före slutdatumet";
        }

        boolean isDoubleBooked = bookingRepository
                .findByRoomId(booking.getRoom().getId())
                .stream()
                .anyMatch(existing ->
                        booking.getStartDate().before(existing.getEndDate()) &&
                                booking.getEndDate().after(existing.getStartDate()));

        if (isDoubleBooked) {
            return "Rummet är redan uppbokat under denna period";
        }

        bookingRepository.save(booking);
        return "Bokningen är nu gjord";
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

    public String updateBooking(Long id, Booking booking) {
        Booking existing = bookingRepository.findById(id).orElse(null);
        if (existing == null) {
            return "Bokning med ID " + id + " finns inte.";
        }

        boolean isDoubleBooked = bookingRepository
                .findByRoomId(booking.getRoom().getId())
                .stream()
                .filter(b -> !b.getId().equals(id))
                .anyMatch(existingBooking ->
                        booking.getStartDate().before(existingBooking.getEndDate()) &&
                                booking.getEndDate().after(existingBooking.getStartDate()));

        if (isDoubleBooked) {
            return "Det finns redan en bokning för detta rum under valt datumintervall.";
        }

        booking.setId(id);
        bookingRepository.save(booking);
        return "Bokningen har uppdaterats.";
    }

    public String deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            return "Bokning finns inte.";
        }

        bookingRepository.deleteById(id);
        return "Bokning har tagits bort.";
    }


}
