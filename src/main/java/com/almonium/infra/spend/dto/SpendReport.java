package com.almonium.infra.spend.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Two views of the same money. The estimated side is our own ledgers priced by our own table, so it knows which
 * feature spent what; the actual side is what OpenAI charged, which knows only project and line item. The gap between
 * the totals is the number that says the price table is stale.
 */
public record SpendReport(
        Instant since,
        Instant until,
        List<EstimatedLine> estimated,
        BigDecimal estimatedUsd,
        List<ActualLine> actual,
        BigDecimal actualUsd,
        Instant actualFetchedAt,
        List<String> warnings) {

    /** {@code estimatedUsd} is null when the model has no price in the table; the tokens are still shown. */
    public record EstimatedLine(
            String source,
            String feature,
            String model,
            long requests,
            long inputTokens,
            long cachedInputTokens,
            long outputTokens,
            BigDecimal estimatedUsd) {}

    public record ActualLine(LocalDate day, String projectId, String lineItem, BigDecimal usd) {}
}
