package org.example.hotellkantarell.restcontroller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    // TODO FIXA BÄTTRE SVAR
    @PostMapping("/booking")
    public ResponseEntity<String> addBooking(@RequestBody Booking booking) {
        boolean success = bookingService.createBooking(booking);
        return success
                ? ResponseEntity.ok("Bokning skapad")
                : ResponseEntity.status(HttpStatus.CONFLICT).
                body("Bokning misslyckades: Rummet är redan bokat eller datumen är ogiltiga");
    }

    @PatchMapping("/booking/{id}")
    public ResponseEntity<String> editBooking(@RequestBody Booking booking, @PathVariable Long id) {
        boolean success = bookingService.updateBooking(id, booking);
        return success
                ? ResponseEntity.ok("Bokning uppdaterad")
                : ResponseEntity.status(HttpStatus.CONFLICT).
                body("Uppdatering misslyckades: Rummet är redan bokat eller bokningen hittades inte");
    }

    @DeleteMapping("/booking/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable long id) {
        boolean success = bookingService.deleteBooking(id);
        return success
                ? ResponseEntity.ok("Bokning borttagen")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).
                body("Radering misslyckades: Bokningen hittades inte");
    }


}
