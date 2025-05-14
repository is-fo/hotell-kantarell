package org.example.hotellkantarell.controller;


import org.example.hotellkantarell.model.Room;
import org.example.hotellkantarell.repository.RoomRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("room/edit/{id}")
    public List<Room> editRoom(@RequestBody Room room) {
        roomRepository.save(room);
        return roomRepository.findAll();
    }

    @RequestMapping("room/delete/{id}")
    public List<Room> deleteById(@PathVariable long id) {
        roomRepository.deleteById(id);
        return roomRepository.findAll();
    }

    @RequestMapping("room/add")
    public List<Room> addRoom(@RequestBody Room room) {
        roomRepository.save(room);
        return roomRepository.findAll();
    }

}
