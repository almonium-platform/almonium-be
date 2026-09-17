package com.almonium.learning.book.dto.response;

import java.util.List;

public record BookRequestQueueDto(
        int open, int inProgress, int published, int declined, int askers, List<BookRequestRow> rows) {}
