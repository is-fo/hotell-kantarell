package org.example.hotellkantarell.controller;

import org.example.hotellkantarell.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void showProfile_withoutUser() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void showProfile_withUser() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", new UserDto(1L, "Test", "test@test.com"));

        mockMvc.perform(get("/profile").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));
    }

    @Test
    void showEditProfile_withUser() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", new UserDto(1L, "Test", "test@test.com"));

        mockMvc.perform(get("/profile/user/update").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("editprofile"));
    }

    @Test
    void editProfile_withValidData() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", new UserDto(1L, "Test", "test@test.com"));

        mockMvc.perform(post("/profile/user/update")
                        .param("name", "New Name")
                        .param("email", "new@email.com")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));
    }

    @Test
    void updatePassword_withValidData() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", new UserDto(1L, "Test", "test@test.com"));

        mockMvc.perform(post("/profile/user/updatepassword")
                        .param("rawPassword", "newpassword123")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));
    }

    @Test
    void deleteBooking_withValidId() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", new UserDto(1L, "Test", "test@test.com"));

        mockMvc.perform(post("/profile/booking/delete")
                        .param("bookingId", "1")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));
    }

    @Test
    void updateBooking_withValidData() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", new UserDto(1L, "Test", "test@test.com"));

        mockMvc.perform(post("/profile/booking/update")
                        .param("bookingId", "1")
                        .param("start", "2025-01-01")
                        .param("end", "2025-01-05")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));
    }
}