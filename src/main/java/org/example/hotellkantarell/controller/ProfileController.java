package org.example.hotellkantarell.controller;

import jakarta.servlet.http.HttpSession;
import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.model.Room;
import org.example.hotellkantarell.model.User;
import org.example.hotellkantarell.service.BookingService;
import org.example.hotellkantarell.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
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
        populateProfile(model, user);

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

    @PostMapping("/profile/booking/delete")
    public String deleteBooking(HttpSession session, @RequestParam Long bookingId, Model model) {
        Booking booking = bookingService.findById(bookingId).orElse(null);
        User user = (User) session.getAttribute("user");
        if (
                booking == null ||
                user == null ||
                !user.getName().equals(booking.getUser().getName()) ||
                !bookingService.deleteBooking(booking.getId())
        ) {
            model.addAttribute("error", "Kunde inte ta bort bokningen.");
            System.err.println("Kunde inte ta bort bokning med id: " + bookingId);
            populateProfile(model, user);
            return "profile";
        }

        return "redirect:/profile";
    }

    @PostMapping("profile/booking/update")
    public String updateBooking(HttpSession session,
                                @RequestParam Long bookingId,
                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
                                Model model) {
        Booking booking = bookingService.findById(bookingId).orElse(null);
        User user = (User) session.getAttribute("user");
        if (booking == null || user == null || !user.getName().equals(booking.getUser().getName())) {
            model.addAttribute("error", "Kunde inte uppdatera bokningen.");
            System.err.println("Kunde inte uppdatera bokning med id: " + bookingId);
            populateProfile(model, user);
            return "profile";
        }

        if (start.after(end)) {
            model.addAttribute("error", "Startdatum måste vara före slutdatum.");
            populateProfile(model, user);
            return "profile";
        }

        booking.setStartDate(start);
        booking.setEndDate(end);

        if (!bookingService.updateBooking(booking.getId(), booking)) {
            model.addAttribute("error", "Kunde inte uppdatera bokningen.");
            System.err.println("Uppdatering misslyckades för bokning med id: " + bookingId);
            populateProfile(model, user);
            return "profile";
        }

        return "redirect:/profile";
    }

    private void populateProfile(Model model, User user) {
        List<Booking> bookings = bookingService.findBookingByUser(user);
        model.addAttribute("bookings", bookings);
        List<Room> rooms = bookings.stream().map(Booking::getRoom).toList();
        model.addAttribute("rooms", rooms);
    }
}
