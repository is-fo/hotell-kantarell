package org.example.hotellkantarell.controller;


import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.repository.BookingRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("bookings")
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
    public List<Booking> addBooking(@RequestBody Booking booking) {
        Booking savedBooking = bookingRepository.save(booking);
        savedBooking.setMessage("Bokningen lyckades");
        return List.of(savedBooking);
    }

    @PostMapping("/booking/edit/{id}")
    public List<Booking> editBooking(@RequestBody Booking booking, @PathVariable long id) {
        booking.setId(id);
        Booking updatedBooking = bookingRepository.save(booking);
        updatedBooking.setMessage("Bokningen uppdaterades");
        return List.of(updatedBooking);
    }

    @DeleteMapping("/booking/delete/{id}")
    public List<Booking> deleteBooking(@PathVariable long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isPresent()) {
            bookingRepository.deleteById(id);
            Booking deletedBooking = booking.get();
            deletedBooking.setMessage("Bokningen togs bort");
            return List.of(deletedBooking);
        } else {
            Booking fakeBooking = new Booking();
            fakeBooking.setMessage("Bokningen hittades inte");
            return List.of(fakeBooking);
        }
    }
}
