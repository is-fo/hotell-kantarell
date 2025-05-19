package org.example.hotellkantarell.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.hotellkantarell.dto.EditPasswordRequest;
import org.example.hotellkantarell.dto.EditProfileRequest;
import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.model.Room;
import org.example.hotellkantarell.model.User;
import org.example.hotellkantarell.service.BookingService;
import org.example.hotellkantarell.service.UserService;
import org.example.hotellkantarell.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
        String username = user.getName();
        String email = user.getEmail();
        model.addAttribute("userDetailsLabel", "Dina uppgifter");
        model.addAttribute("nameLabel", "Namn: ");
        model.addAttribute("emailLabel", "Mailadress: ");
        model.addAttribute("nameValue", username);
        model.addAttribute("emailValue", email);
        populateProfile(model, user);

        return "profile";
    }

    @GetMapping("/profile/user/update")
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

    @PostMapping("/profile/user/update")
    public String editProfile(@ModelAttribute @Valid EditProfileRequest editProfileRequest, BindingResult result, HttpSession session, Model model) {

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }
        if (result.hasErrors()) {
            model.addAttribute("userDetails", "Ändra dina uppgifter");
            model.addAttribute("nameLabel", "Namn: ");
            model.addAttribute("emailLabel", "Mailadress: ");
            return "editprofile";
        }

        User updated = userService.editProfile(currentUser, editProfileRequest);
        session.setAttribute("user", updated);

        return "redirect:/profile";
    }

    @PostMapping("/profile/user/updatepassword")
    public String updatePassword(@ModelAttribute @Valid EditPasswordRequest editPasswordRequest, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        User updated = userService.editPassword(currentUser, editPasswordRequest);
        session.setAttribute("user", updated);

        return "redirect:/profile";
    }

    @GetMapping("/profile/user/updatepassword")
    public String showUpdatePasswordForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        return "editpassword";
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
            model.addAttribute("error", "Något gick snett. Ladda om sidan.");
            System.err.println("Kaffe i servern: " + bookingId);
            populateProfile(model, user);
            return "profile";
        }

        if (start.after(end)) {
            model.addAttribute("error", "Startdatum måste vara före slutdatum.");
            populateProfile(model, user);
            return "profile";
        }

        if (start.before(DateUtil.nDaysInFuture(-1))) {
            model.addAttribute("error", "Startdatum måste vara idag eller i framtiden");
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
