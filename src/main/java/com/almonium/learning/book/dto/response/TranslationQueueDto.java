package com.almonium.learning.book.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record TranslationQueueDto(
        int open,
        int running,
        int published,
        int declined,
        BigDecimal monthSpendUsd,
        BigDecimal monthBudgetUsd,
        boolean budgetExhausted,
        Instant monthStartsAt,
        List<TranslationQueueRow> rows,
        List<String> warnings) {}
