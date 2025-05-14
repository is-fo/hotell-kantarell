package org.example.hotellkantarell.dto;

public record LoginRequest(
        String email,
        String rawPassword
) {
}
