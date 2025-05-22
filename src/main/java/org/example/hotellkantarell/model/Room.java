package org.example.hotellkantarell.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

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

    @Min(0)
    Integer area = 10;

    @URL(host = "imgur")
    String imageUrl;

    @Min(0)
    Integer pricePerNight;

    @Size(min = 3)
    String roomNumber;

    public Room(Integer beds, Integer extraBeds, Integer area) {
        this.beds = beds;
        this.extraBeds = extraBeds;
        this.area = area;
    }

}
