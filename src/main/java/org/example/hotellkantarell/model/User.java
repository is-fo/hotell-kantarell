package org.example.hotellkantarell.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User() {

    }
}
