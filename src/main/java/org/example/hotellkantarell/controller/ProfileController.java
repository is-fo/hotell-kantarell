package org.example.hotellkantarell.controller;

import jakarta.servlet.http.HttpSession;
import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.model.Room;
import org.example.hotellkantarell.model.User;
import org.example.hotellkantarell.service.BookingService;
import org.example.hotellkantarell.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ProfileController {
    private final UserService userService;
    private final BookingService bookingService;

    public ProfileController(UserService userService, BookingService bookingService) {
        this.userService = userService;
        this.bookingService = bookingService;
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        List<Booking> bookings = bookingService.findBookingByUser(user);
        model.addAttribute("bookings", bookings);
        List<Room> rooms = bookings.stream().map(Booking::getRoom).toList();
        model.addAttribute("rooms", rooms);

        return "profile";
    }

    @PostMapping("/profile/user/delete")
    public String deleteUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        return userService.deleteUser(user) ? "redirect:/register" : "redirect:/profile";
    }
}
