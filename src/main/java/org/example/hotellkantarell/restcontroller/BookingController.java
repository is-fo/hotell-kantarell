package org.example.hotellkantarell.restcontroller;


import org.springframework.ui.Model;
import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.model.Room;
import org.example.hotellkantarell.repository.BookingRepository;
import org.example.hotellkantarell.service.BookingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingRepository bookingRepository;
    private final BookingService bookingService;

    public BookingController(BookingRepository bookingRepository, BookingService bookingService) {
        this.bookingRepository = bookingRepository;
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @PostMapping("/booking")
    public String addBooking(@RequestBody Booking booking) {
        return bookingService.createBooking(booking);
    }

    @PatchMapping("/booking/{id}")
    public String editBooking(@RequestBody Booking booking, @PathVariable Long id) {
        return bookingService.updateBooking(id, booking);
    }

    @DeleteMapping("/booking/{id}")
    public String deleteBooking(@PathVariable long id) {
        return bookingService.deleteBooking(id);
    }



}
