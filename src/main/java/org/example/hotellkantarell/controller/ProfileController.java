package org.example.hotellkantarell.controller;

import jakarta.servlet.http.HttpSession;
import org.example.hotellkantarell.dto.RegisterRequest;
import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.model.Room;
import org.example.hotellkantarell.model.User;
import org.example.hotellkantarell.service.BookingService;
import org.example.hotellkantarell.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
        String username = user.getName();
        String email = user.getEmail();
        model.addAttribute("userDetailsLabel", "Dina uppgifter");
        model.addAttribute("nameLabel", "Namn: ");
        model.addAttribute("emailLabel", "Mailadress: ");
        model.addAttribute("nameValue", username);
        model.addAttribute("emailValue", email);
        List<Booking> bookings = bookingService.findBookingByUser(user);
        model.addAttribute("bookings", bookings);
        List<Room> rooms = bookings.stream().map(Booking::getRoom).toList();
        model.addAttribute("rooms", rooms);

        return "profile";
    }

    @GetMapping("/editprofile")
    public String showEditProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("userDetails", "Ändra dina uppgifter");
        model.addAttribute("nameLabel", "Namn: ");
        model.addAttribute("emailLabel", "Mailadress: ");
        model.addAttribute("nameValue", user.getName());
        model.addAttribute("emailValue", user.getEmail());

        return "editprofile";
    }

    @PostMapping("/editprofile")
    public String editProfile(@ModelAttribute RegisterRequest registerRequest, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        User updated = userService.editProfile(currentUser, registerRequest);
        session.setAttribute("user", updated);

        return "redirect:/profile";
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
