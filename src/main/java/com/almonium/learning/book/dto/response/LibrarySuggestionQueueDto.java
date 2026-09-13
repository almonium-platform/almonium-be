package com.almonium.learning.book.dto.response;

import java.util.List;

public record LibrarySuggestionQueueDto(
        int open, int ingesting, int published, int declined, List<LibrarySuggestionRow> rows, List<String> warnings) {}
