package com.almonium.learning.book.dto.response;

import java.time.Instant;

/** The caller's private-book allowance for the current subscription month. */
public record BookImportQuotaDto(int limit, int used, Instant periodStartsAt, Instant periodEndsAt) {}
