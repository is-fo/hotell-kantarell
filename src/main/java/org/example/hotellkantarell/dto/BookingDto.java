package org.example.hotellkantarell.dto;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public record BookingDto(
        Long id,
        RoomDto room,
        UserDto user,
        Date startDate,
        Date endDate,
        Long totalPrice
) {

    public BookingDto {
        totalPrice = setTotalPrice();
    }

    Long setTotalPrice() {
        assert startDate != null;
        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        assert endDate != null;
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        assert room != null;
        return room.pricePerNight() * ChronoUnit.DAYS.between(start, end);
    }
}
