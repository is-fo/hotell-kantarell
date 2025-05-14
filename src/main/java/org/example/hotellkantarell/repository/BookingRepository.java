package org.example.hotellkantarell.repository;

import org.example.hotellkantarell.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {

}
