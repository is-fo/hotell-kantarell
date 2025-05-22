package org.example.hotellkantarell.restcontroller;

import org.example.hotellkantarell.model.Room;
import org.example.hotellkantarell.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class RoomControllerTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomController roomController;

    private Room testRoom;

    @BeforeEach
    void setup() {
        testRoom = new Room(2, 1, 20);
        testRoom.setId(1L);
    }

    @Test
    void getAllRooms() throws Exception {
        when(roomRepository.findAll()).thenReturn(List.of(testRoom));

        List<Room> result = roomController.getAllRooms();

        assertEquals(1, result.size());
        assertEquals(2, result.getFirst().getBeds());
    }

    @Test
    void addRoom_returnsSuccessMessage() throws Exception {
        when(roomRepository.save(testRoom)).thenReturn(testRoom);

        String result = roomController.addRoom(testRoom);

        assertTrue(result.contains("La till"));
        verify(roomRepository).save(testRoom);
    }

    @Test
    void editRoom_returnsUpdatedMessage() throws Exception {
        when(roomRepository.save(any(Room.class))).thenReturn(testRoom);

        String result = roomController.editRoom(testRoom, 1L);

        assertTrue(result.contains("Uppdaterade"));
        assertEquals(1L, testRoom.getId());
    }

    @Test
    void deleteById_success() throws Exception {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(testRoom));

        String result = roomController.deleteById(1L);

        assertTrue(result.contains("Tog bort"));
        verify(roomRepository).delete(testRoom);
    }

    @Test
    void deleteById_notFound() throws Exception {
        when(roomRepository.findById(2L)).thenReturn(Optional.empty());

        String result = roomController.deleteById(2L);

        assertEquals("Kunde inte hitta ett rum med id: 2", result);
    }
}