package org.example.hotellkantarell.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Min(1)
    @Max(2)
    Integer beds;

    @Min(0)
    @Max(2)
    Integer extraBeds;

    @Transient
    String message;

    public Room(Integer beds, Integer extraBeds) {
        this.beds = beds;
        this.extraBeds = extraBeds;
    }

}
