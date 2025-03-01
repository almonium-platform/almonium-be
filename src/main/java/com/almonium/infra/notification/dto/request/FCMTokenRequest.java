package com.almonium.infra.notification.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FCMTokenRequest(@NotBlank String token, @NotBlank String deviceType) {}
