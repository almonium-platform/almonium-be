package com.linguarium.user.core.dto;

import jakarta.validation.constraints.NotBlank;

public record UsernameUpdateRequest(@NotBlank String newUsername) {}
