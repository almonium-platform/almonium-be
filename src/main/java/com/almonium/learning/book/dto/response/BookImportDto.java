package com.almonium.learning.book.dto.response;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.model.enums.BookImportStatus;
import java.time.Instant;
import java.util.UUID;

public record BookImportDto(
        UUID id,
        String title,
        String author,
        String description,
        Language language,
        Integer publicationYear,
        BookImportStatus status,
        int progress,
        int wordCount,
        String error,
        Instant createdAt,
        Instant updatedAt) {}
