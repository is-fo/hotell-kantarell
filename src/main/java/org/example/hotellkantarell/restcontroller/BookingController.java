package org.example.hotellkantarell.restcontroller;


import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.repository.BookingRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bookings")
public class BookingController {

   private final BookingRepository bookingRepository;

    BookingController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @GetMapping
    public List<Booking> getAllBookings(){
        return bookingRepository.findAll();
    }

    @PostMapping("/booking")
    public String addBooking(@RequestBody Booking booking) {

        Booking savedBooking = bookingRepository.save(booking);
        return "La till: " + savedBooking;
    }

    @PatchMapping("/booking/{id}")
    public String editBooking(@RequestBody Booking booking, @PathVariable Long id) {
        booking.setId(id);
        try {
            bookingRepository.save(booking);
        } catch (Exception e) {
            e.printStackTrace();
            return "Misslyckades med att uppdatera: " + e.getMessage();
        }
        return "Uppdaterade: " + booking;
    }

    @DeleteMapping("/booking/{id}")
    public String deleteBooking(@PathVariable long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isPresent()) {
            bookingRepository.deleteById(id);
            return "Tog bort: " + booking.get();
        } else {
            return "Hittade inte bokning med id: " + id;
        }
    }
}
