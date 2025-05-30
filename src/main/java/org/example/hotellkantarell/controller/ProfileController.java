package org.example.hotellkantarell.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.hotellkantarell.dto.*;
import org.example.hotellkantarell.status.BookingStatus;
import org.example.hotellkantarell.service.BookingService;
import org.example.hotellkantarell.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        UserDto user = (UserDto) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        populateProfile(model, user);

        return "profile";
    }

    @GetMapping("/profile/user/update")
    public String showEditProfile(HttpSession session, Model model) {
        UserDto user = (UserDto) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("userDetails", "Ändra dina uppgifter");
        model.addAttribute("nameLabel", "Namn: ");
        model.addAttribute("emailLabel", "Mailadress: ");
        model.addAttribute("nameValue", user.name());
        model.addAttribute("emailValue", user.email());

        return "editprofile";
    }

    @PostMapping("/profile/user/update")
    public String editProfile(@ModelAttribute @Valid EditProfileRequest editProfileRequest, BindingResult result, HttpSession session, RedirectAttributes redirectAttributes, Model model) {

        UserDto currentUser = (UserDto) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }
        if (result.hasErrors()) {
            model.addAttribute("userDetails", "Ändra dina uppgifter");
            model.addAttribute("nameLabel", "Namn: ");
            model.addAttribute("emailLabel", "Mailadress: ");
            return "editprofile";
        }

        UserDto updatedUser = userService.editProfile(currentUser, editProfileRequest);
        if (updatedUser != null) {
            redirectAttributes.addFlashAttribute("success", "Användaruppgifter uppdaterade.");
            session.setAttribute("user", updatedUser);
        } else {
            redirectAttributes.addFlashAttribute("error", "Den angivna mailadressen är upptagen.");
        }

        return "redirect:/profile";
    }

    @PostMapping("/profile/user/updatepassword")
    public String updatePassword(@ModelAttribute EditPasswordRequest editPasswordRequest, RedirectAttributes redirectAttributes, HttpSession session) {
        UserDto currentUser = (UserDto) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        if (userService.editPassword(currentUser, editPasswordRequest)) {
            redirectAttributes.addFlashAttribute("success", "Ändrade lösenordet!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Lösenordet måste vara minst 6 karaktärer.");
        }
        return "redirect:/profile";
    }

    @GetMapping("/profile/user/updatepassword")
    public String showUpdatePasswordForm(HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        return "editpassword";
    }

    @PostMapping("/profile/user/delete")
    public String deleteUser(HttpSession session, RedirectAttributes redirectAttributes) {
        UserDto user = (UserDto) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (userService.deleteUser(user, session)) {
            return "redirect:/register";
        } else {
            redirectAttributes.addFlashAttribute("error", "Du får inte ta bort ditt konto om du har aktiva bokningar.");
            return "redirect:/profile";
        }
    }

    @PostMapping("/profile/booking/delete")
    public String deleteBooking(HttpSession session, @RequestParam Long bookingId, Model model) {
        UserDto user = (UserDto) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        boolean success = bookingService.deleteBooking(bookingId, user);
        if (!success) {
            model.addAttribute("error", "Kunde inte ta bort bokningen. Försök igen.");
            System.err.println("Kunde inte ta bort bokning med id: " + bookingId);
        }

        return "redirect:/profile";
    }


    @PostMapping("profile/booking/update")
    public String updateBooking(HttpSession session,
                                @RequestParam Long bookingId,
                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
                                RedirectAttributes redirectAttributes) {
        UserDto user = (UserDto) session.getAttribute("user");

        final var result = bookingService.updateBooking(user, bookingId, start, end);
        if (!result.equals(BookingStatus.SUCCESS)) {
            redirectAttributes.addFlashAttribute("error", result.getMessage());
            System.err.println("Uppdatering misslyckades för bokning med id: " + bookingId + "Fel: " + result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("success", result.getMessage());
        }
        return "redirect:/profile";
    }

    private void populateProfile(Model model, UserDto user) {
        model.addAttribute("userDetailsLabel", "Dina uppgifter");
        model.addAttribute("nameLabel", "Namn: ");
        model.addAttribute("emailLabel", "Mailadress: ");
        model.addAttribute("nameValue", user.name());
        model.addAttribute("emailValue", user.email());
        List<BookingDto> bookingsDto = bookingService.findBookingByUser(user);
        model.addAttribute("bookingsDto", bookingsDto);
        List<RoomDto> rooms = bookingsDto.stream().map(BookingDto::room).toList();
        model.addAttribute("rooms", rooms);
    }
}
