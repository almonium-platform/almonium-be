package com.linguarium.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UsernameUpdateRequest(@NotBlank String newUsername) {
}
