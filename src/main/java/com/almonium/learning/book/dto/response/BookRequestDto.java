package com.almonium.learning.book.dto.response;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.model.enums.BookRequestStatus;
import java.time.Instant;
import java.util.UUID;

public record BookRequestDto(
        UUID id, String title, String author, Language language, BookRequestStatus status, Instant createdAt) {}
