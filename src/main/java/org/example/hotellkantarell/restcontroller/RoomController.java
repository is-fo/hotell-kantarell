package org.example.hotellkantarell.restcontroller;


import org.example.hotellkantarell.model.Room;
import org.example.hotellkantarell.repository.RoomRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomRepository roomRepository;

    RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @PatchMapping("/room/{id}")
    public String editRoom(@RequestBody Room room, @PathVariable Long id) {
        room.setId(id);
        try {
            roomRepository.save(room);
        } catch (Exception e) {
            e.printStackTrace();
            return "Misslyckades med att uppdatera: " + e.getMessage();
        }
        return "Uppdaterade: " + room;
    }

    @DeleteMapping("/room/{id}")
    public String deleteById(@PathVariable Long id) {
        Optional<Room> room = roomRepository.findById(id);
        if (room.isPresent()) {
            roomRepository.delete(room.get());
            return "Tog bort: " + room.get();
        } else {
            return "Kunde inte hitta ett rum med id: " + id;
        }
    }

    @PostMapping("/room")
    public String addRoom(@RequestBody Room room) {
        try {
            roomRepository.save(room);
        } catch (Exception e) {
            e.printStackTrace();
            return "Kunde inte lägga till rummet: " + e.getMessage();
        }
        return "La till: " + room;
    }

}
