package org.example.hotellkantarell.controller;

import jakarta.servlet.http.HttpSession;
import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.model.Room;
import org.example.hotellkantarell.model.User;
import org.example.hotellkantarell.repository.RoomRepository;
import org.example.hotellkantarell.service.BookingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class BookingPageController {
    final BookingService bookingService;
    private final RoomRepository roomRepository;

    public BookingPageController(BookingService bookingService, RoomRepository roomRepository) {
        this.bookingService = bookingService;
        this.roomRepository = roomRepository;
    }

    @GetMapping("/start")
    public String findAvailableRooms(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
            @RequestParam(required = false) Integer guests,
            Model model)
    {
        if (start != null && end != null && guests != null) {
            List<Room> results = bookingService.findAvailableRooms(start, end, guests);
            model.addAttribute("results", results);
            model.addAttribute("start", start);
            model.addAttribute("end", end);
        }
        return "start";
    }

    @PostMapping("/book")
    public String bookRoom(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
                           @RequestParam("room.id") Long roomId, HttpSession session, Model model)
    {
        User user = (User) session.getAttribute("user");

        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if (optionalRoom.isEmpty()) {
            model.addAttribute("error", "Rummet kunde inte hittas.");
            return "start";
        }

        Room room = optionalRoom.get();
        Booking booking = new Booking(room, user, start, end);

        bookingService.createBooking(booking);
        return "start";
    }


}
