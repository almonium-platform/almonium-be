package com.almonium.learning.book.dto.response;

import java.time.Instant;
import java.util.UUID;

public record TranslationOrderDto(UUID id, UUID userId, UUID bookId, String language, Instant createdAt) {}
