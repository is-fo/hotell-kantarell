package org.example.hotellkantarell.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "Namn är obligatoriskt")
        @Pattern(regexp = "^[a-zA-ZåÅäÄöÖ '\\-]+$", message = "Ogiltigt namn")
        String name,

        @Email(message = "Felaktigt format på e-postadressen")
        String email,

        @Size(min = 6, message = "Lösenordet måste innehålla minst 6 tecken")
        String rawPassword
) {
}
