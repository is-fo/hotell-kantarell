package org.example.hotellkantarell.controller;

import jakarta.servlet.http.HttpSession;
import org.example.hotellkantarell.dto.BookingDto;
import org.example.hotellkantarell.dto.RoomDto;
import org.example.hotellkantarell.dto.UserDto;
import org.example.hotellkantarell.repository.RoomRepository;
import org.example.hotellkantarell.service.BookingService;
import org.example.hotellkantarell.service.RoomService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.example.hotellkantarell.status.BookingStatus.EXPIRED_DATE;
import static org.example.hotellkantarell.status.BookingStatus.REVERSE_DATE;

@Controller
public class BookingPageController {

    final BookingService bookingService;
    final RoomService roomService;

    public BookingPageController(BookingService bookingService, RoomService roomService) {
        this.bookingService = bookingService;
        this.roomService = roomService;
    }

    @GetMapping("/start")
    public String findAvailableRooms(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
            @RequestParam(required = false) Integer guests,
            Model model) {
        if (start != null && end != null && guests != null) {
            List<RoomDto> results = bookingService.findAvailableRooms(start, end, guests);
            model.addAttribute("results", results);
            model.addAttribute("start", start);
            model.addAttribute("end", end);
        }
        return "start";
    }

    @PostMapping("/book")
    public String bookRoom(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
                           @RequestParam("room.id") Long roomId, HttpSession session, Model model) {
        UserDto user = (UserDto) session.getAttribute("user");


        RoomDto roomDto = roomService.findById(roomId);
        if (roomDto == null) {
            model.addAttribute("error", "Rummet kunde inte hittas.");
            return "start";
        }

        BookingDto booking = new BookingDto(null, roomDto, user, start, end);

        if (!bookingService.createBooking(booking)) {
            model.addAttribute("error", "Kunde inte genomföra bokningen. " +
                    "Se till att startdatumet är innan slutdatum och inte i dåtiden.");
        } else {
            model.addAttribute("success", "Bokningen lyckades. Gå till din profil för att se dina aktuella bokningar.");
        }
        return "start";
    }
}
