package com.almonium.learning.book.dto.response;

import com.almonium.analyzer.translator.model.enums.Language;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TranslationOrderRequest(@Positive long bookId, @NotNull Language language) {}
