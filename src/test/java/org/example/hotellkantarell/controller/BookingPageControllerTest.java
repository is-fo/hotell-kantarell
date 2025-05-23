package org.example.hotellkantarell.controller;

import org.example.hotellkantarell.HotellKantarellApplication;
import org.example.hotellkantarell.service.BookingService;
import org.example.hotellkantarell.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookingPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookingService bookingService;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    void setup() {
        reset(bookingService);
    }

    @Test
    void findAvailableRooms_withValidParams() throws Exception {
        when(bookingService.validDates(any(), any())).thenReturn(true);
        when(bookingService.findAvailableRooms(any(), any(), anyInt()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/start")
                        .param("start", "2025-06-01")
                        .param("end", "2025-06-03")
                        .param("guests", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name("start"))
                .andExpect(model().attributeExists("results"))
                .andExpect(model().attributeExists("start"))
                .andExpect(model().attributeExists("end"));
    }

    @Test
    void findAvailableRooms_withInvalidDates() throws Exception {
        when(bookingService.validDates(any(), any())).thenReturn(false);

        mockMvc.perform(get("/start")
                        .param("start", "2020-01-01")
                        .param("end", "2020-01-02")
                        .param("guests", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name("start"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeDoesNotExist("results"));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public BookingService bookingService() {
            return mock(BookingService.class);
        }

        @Bean
        public RoomService roomService() {
            return mock(RoomService.class);
        }
    }
}