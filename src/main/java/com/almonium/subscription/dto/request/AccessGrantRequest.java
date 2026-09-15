package com.almonium.subscription.dto.request;

import com.almonium.subscription.model.entity.enums.Entitlement;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record AccessGrantRequest(
        @NotNull Entitlement entitlement,
        Instant expiresAt,
        @NotBlank String reason) {}
