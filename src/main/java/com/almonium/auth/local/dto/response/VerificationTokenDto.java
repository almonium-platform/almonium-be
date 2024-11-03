package com.almonium.auth.local.dto.response;

import java.time.Instant;

public record VerificationTokenDto(String email, Instant expiresAt) {}
