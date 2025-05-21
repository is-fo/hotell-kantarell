package org.example.hotellkantarell.service;

import org.example.hotellkantarell.dto.RoomDto;
import org.example.hotellkantarell.mapper.RoomMapper;
import org.example.hotellkantarell.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    private final RoomMapper roomMapper;
    public RoomService(RoomRepository roomRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    public RoomDto findById(Long id) {
        final var result = roomRepository.findById(id).orElse(null);
        return result != null ? roomMapper.roomToDto(result) : null;
    }
}
