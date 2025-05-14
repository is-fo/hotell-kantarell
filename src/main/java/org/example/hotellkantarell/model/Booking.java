package org.example.hotellkantarell.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "room_id")
    Room room;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

    Date startDate;

    Date endDate;

    @Transient
    String message;

    public Booking(Room room, User user, Date startDate, Date endDate) {
        this.room = room;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
