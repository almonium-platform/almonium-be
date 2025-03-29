package com.almonium.learning.book.dto.response;

import java.time.Instant;
import java.util.UUID;

public record TranslationOrderDto(String id, UUID userId, Long bookId, String language, Instant createdAt) {}
