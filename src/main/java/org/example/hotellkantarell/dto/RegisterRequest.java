package org.example.hotellkantarell.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "Name is required")
        String name,

        @Email(message = "Invalid email format")
        String email,

        @Size(min = 6, message = "Password must be atleast 6 characters")
        String rawPassword
) {
}
