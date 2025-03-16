package com.almonium.learning.book.dto.response;

import java.time.Instant;
import java.util.UUID;

public record LearnerBookProgressDto(
        UUID id, UUID bookId, int progressPercentage, Instant startedAt, Instant lastReadAt) {}
