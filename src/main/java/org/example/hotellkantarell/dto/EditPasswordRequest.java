package org.example.hotellkantarell.dto;

import jakarta.validation.constraints.Size;

public record EditPasswordRequest(

        @Size(min = 6, message = "Password must be at least 6 characters")
        String rawPassword
) {
}