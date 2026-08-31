package com.almonium.learning.book.dto.response;

import java.time.Instant;

/** The caller's translation-request allowance for the current subscription month. */
public record TranslationRequestQuotaDto(int limit, int used, Instant periodStartsAt, Instant periodEndsAt) {}
