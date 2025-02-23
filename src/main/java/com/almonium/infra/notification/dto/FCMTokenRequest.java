package com.almonium.infra.notification.dto;

import jakarta.validation.constraints.NotBlank;

public record FCMTokenRequest(@NotBlank String token, @NotBlank String deviceType) {}
