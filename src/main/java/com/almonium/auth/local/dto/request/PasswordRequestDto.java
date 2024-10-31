package com.almonium.auth.local.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordRequestDto(@NotBlank @Size(min = 8, max = 30) String password) {}
