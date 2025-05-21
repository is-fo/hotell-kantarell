package org.example.hotellkantarell.dto;

public record RoomDto(
        Long id,
        Integer beds,
        Integer extraBeds,
        Integer area,
        String imageUrl,
        Integer pricePerNight,
        String roomNumber
) {
}
