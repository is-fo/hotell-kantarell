package org.example.hotellkantarell.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public record EditProfileRequest(

        @Pattern(regexp = "^[a-zA-ZåÅäÄöÖ '\\-]+$", message = "Ogiltigt namn")
        @Size(min = 3, max =50, message = "Namnet måste vara minst 3 tecken långt")
        String name,

        @Email(message = "Felaktigt format på e-postadressen")
        String email

) {
}
