package org.example.hotellkantarell.service;

import org.example.hotellkantarell.dto.RoomDto;
import org.example.hotellkantarell.mapper.RoomMapper;
import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.repository.BookingRepository;
import org.example.hotellkantarell.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.hotellkantarell.util.DateUtil.*;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final BookingRepository bookingRepository;

    public RoomService(RoomRepository roomRepository, RoomMapper roomMapper, BookingRepository bookingRepository) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
        this.bookingRepository = bookingRepository;
    }

    public RoomDto findById(Long id) {
        final var result = roomRepository.findById(id).orElse(null);
        return result != null ? roomMapper.roomToDto(result) : null;
    }

    public List<RoomDto> findAvailableRooms(Date startDate, Date endDate, int guests) {
        Date start = setTime(startDate, 16);
        Date end = setTime(endDate, 12);

        List<RoomDto> allRooms = roomRepository.findAll().stream().map(roomMapper::roomToDto).toList();

        return allRooms.stream()
                .filter(room -> {
                    int capacity = room.beds() + room.extraBeds();
                    return guests <= capacity;
                })
                .filter(room -> {
                    List<Booking> bookings = bookingRepository.findByRoomId(room.id());
                    for (Booking booking : bookings) {
                        if (!(end.compareTo(booking.getStartDate()) <= 0 || start.compareTo(booking.getEndDate()) >= 0)) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }
}
