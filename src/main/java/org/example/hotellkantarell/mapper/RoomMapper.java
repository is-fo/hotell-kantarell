package org.example.hotellkantarell.mapper;


import org.example.hotellkantarell.dto.RoomDto;
import org.example.hotellkantarell.model.Room;
import org.springframework.stereotype.Service;

@Service
public class RoomMapper {

    public RoomDto roomToDto(Room room) {
        return new RoomDto(
                room.getId(),
                room.getBeds(),
                room.getExtraBeds(),
                room.getArea(),
                room.getImageUrl(),
                room.getPricePerNight(),
                room.getRoomNumber());
    }

    public Room dtoToRoom(RoomDto roomDto) {
        return new Room(
                roomDto.id(),
                roomDto.beds(),
                roomDto.extraBeds(),
                roomDto.area(),
                roomDto.imageUrl(),
                roomDto.pricePerNight(),
                roomDto.roomNumber());
    }
}
