package org.example.hotellkantarell.repository;

import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Booking> findByRoomId(Long roomId);
}
