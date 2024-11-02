package com.almonium.auth.local.dto.response;

import java.time.LocalDateTime;

public record VerificationTokenDto(String email, LocalDateTime expiresAt) {}
