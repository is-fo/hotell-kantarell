package org.example.hotellkantarell.controller;

import org.example.hotellkantarell.model.Room;
import org.example.hotellkantarell.repository.BookingRepository;
import org.example.hotellkantarell.service.BookingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
public class BookingPageController {
    final BookingService bookingService;
    final BookingRepository bookingRepository;

    public BookingPageController(BookingService bookingService, BookingRepository bookingRepository) {
        this.bookingService = bookingService;
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/start")
    public String findAvailableRooms(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
            @RequestParam(required = false) Integer guests,
            Model model
    ) {
        if (start != null && end != null && guests != null) {
            List<Room> results = bookingService.findAvailableRooms(start, end, guests);
            model.addAttribute("results", results);
        }
        return "start";
    }


}
