package org.example.hotellkantarell.restcontroller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RoomControllerTest {/*

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllRooms() throws Exception {
        mockMvc.perform(get("/rooms"))
                .andExpect(status().isOk());
    }

    @Test
    void addRoom_withValidData() throws Exception {
        mockMvc.perform(post("/rooms/room")
                        .contentType("application/json")
                        .content("{\"beds\":2,\"extraBeds\":1,\"area\":25,\"imageUrl\":\"test.jpg\",\"pricePerNight\":1000,\"roomNumber\":\"101\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void editRoom_withValidData() throws Exception {
        mockMvc.perform(patch("/rooms/room/1")
                        .contentType("application/json")
                        .content("{\"beds\":3,\"extraBeds\":0,\"area\":30,\"imageUrl\":\"updated.jpg\",\"pricePerNight\":1200,\"roomNumber\":\"101\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteRoom_withExistingId() throws Exception {
        mockMvc.perform(delete("/rooms/room/1"))
                .andExpect(status().isOk());
    }*/
}