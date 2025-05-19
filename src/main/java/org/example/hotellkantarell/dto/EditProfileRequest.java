package org.example.hotellkantarell.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record EditProfileRequest(

        @NotBlank(message = "Name is required")
        @Pattern(regexp = "^[a-zA-ZåÅäÄöÖ '\\-]+$", message = "Ogiltigt namn")
        String name,

        @Email(message = "Invalid email format")
        String email

) {
}
