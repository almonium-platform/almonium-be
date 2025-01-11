package com.almonium.user.core.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UsernameUpdateRequest(@NotBlank String username) {}
