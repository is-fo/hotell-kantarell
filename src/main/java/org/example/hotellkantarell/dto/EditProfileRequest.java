package org.example.hotellkantarell.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public record EditProfileRequest(

        @Pattern(regexp = "^[a-zA-ZåÅäÄöÖ '\\-]+$", message = "Ogiltigt namn")
        @Size(min = 3, max = 50, message = "At least 3 Characters for name")
        String name,

        @Email(message = "Invalid email format")
        String email
) {
}
