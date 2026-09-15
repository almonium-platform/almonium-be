package com.almonium.learning.book.dto.request;

import jakarta.validation.constraints.NotBlank;

public record QuotaResetRequest(@NotBlank String reason) {}
