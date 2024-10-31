package com.almonium.auth.common.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailRequestDto(@NotBlank @Email String email) {}
