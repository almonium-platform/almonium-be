package com.almonium.learning.book.dto.response;

import java.math.BigDecimal;

/** One line of the processor's AI run ledger, summed per purpose and model, priced by the processor's own table. */
public record BooksSpendLine(
        String purpose,
        String model,
        long runs,
        long inputTokens,
        long cachedInputTokens,
        long outputTokens,
        long reasoningTokens,
        BigDecimal estimatedCostUsd) {}
