package org.example.hotellkantarell.dto;

import jakarta.validation.constraints.Size;

public record EditPasswordRequest(

    @Size(min = 6, message = "Lösenordet måste innehålla minst 6 tecken")
    String rawPassword

) {
}