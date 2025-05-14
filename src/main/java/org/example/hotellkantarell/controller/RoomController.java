package org.example.hotellkantarell.controller;


import org.example.hotellkantarell.model.Room;
import org.example.hotellkantarell.repository.RoomRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("rooms")
public class RoomController {

    private final RoomRepository roomRepository;

    RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @PostMapping("room/edit/{id}")
    public List<Room> editRoom(@RequestBody Room room, @PathVariable long id) {
        room.setId(id);
        Room updatedRoom = roomRepository.save(room);
        updatedRoom.setMessage("Rummet uppdaterades");
        return List.of(updatedRoom);
    }

    @DeleteMapping("room/delete/{id}")
    public List<Room> deleteById(@PathVariable long id) {
        Optional<Room> room = roomRepository.findById(id);
        if (room.isPresent()) {
            Room deletedRoom = room.get();
            deletedRoom.setMessage("Rummet togs bort");
            return List.of(deletedRoom);
        } else {
            Room fakeRoom = new Room();
            fakeRoom.setMessage("Rummet hittas inte");
            return List.of(fakeRoom);
        }
    }

    @PostMapping("room")
    public List<Room> addRoom(@RequestBody Room room) {
        Room savedRoom = roomRepository.save(room);
        savedRoom.setMessage("Rummet lades till");
        return List.of(savedRoom);
    }

}
