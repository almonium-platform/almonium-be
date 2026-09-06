package com.almonium.infra.spend.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.client.exception.ApiIntegrationException;
import com.almonium.config.properties.OpenAiProperties;
import com.almonium.infra.spend.client.OpenAiCostsClient;
import com.almonium.infra.spend.dto.SpendReport;
import com.almonium.infra.spend.dto.SpendReport.ActualLine;
import com.almonium.infra.spend.dto.SpendReport.EstimatedLine;
import com.almonium.learning.almo.repository.AlmoTurnRepository;
import com.almonium.learning.book.service.BookProcessorClient;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Puts the two sides of the model bill on one page: our ledgers priced by our table, feature by feature, and what
 * OpenAI charged. An upstream that cannot be reached leaves its side empty with a warning rather than failing the page.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SpendReportService {
    static final String ALMO = "almo";
    static final String BOOKS = "books";
    static final BigDecimal TOKENS_PER_PRICE_UNIT = BigDecimal.valueOf(1_000_000);
    static final int USD_SCALE = 6;

    AlmoTurnRepository turnRepository;
    BookProcessorClient bookProcessor;
    OpenAiCostsClient costsClient;
    OpenAiProperties properties;
    Map<LocalDate, FetchedCosts> costsByWindowStart = new ConcurrentHashMap<>();

    record FetchedCosts(Instant fetchedAt, List<ActualLine> lines) {}

    /** The last {@code days} calendar days in UTC, today included. */
    public SpendReport report(int days) {
        Instant until = Instant.now();
        LocalDate firstDay = LocalDate.now(ZoneOffset.UTC).minusDays(days - 1L);
        Instant since = firstDay.atStartOfDay(ZoneOffset.UTC).toInstant();
        List<String> warnings = new ArrayList<>();

        List<EstimatedLine> estimated = new ArrayList<>(almoLines(since, until, warnings));
        estimated.addAll(booksLines(since, until, warnings));
        BigDecimal estimatedUsd = estimated.stream()
                .map(EstimatedLine::estimatedUsd)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        FetchedCosts actual = actualCosts(firstDay, since, days, warnings);
        List<ActualLine> actualLines = actual == null ? List.of() : actual.lines();
        BigDecimal actualUsd = actualLines.stream().map(ActualLine::usd).reduce(BigDecimal.ZERO, BigDecimal::add);

        return new SpendReport(
                since,
                until,
                estimated,
                estimatedUsd,
                actualLines,
                actualUsd,
                actual == null ? null : actual.fetchedAt(),
                warnings);
    }

    private List<EstimatedLine> almoLines(Instant since, Instant until, List<String> warnings) {
        return turnRepository.spendBetween(since, until).stream()
                .map(line -> {
                    OpenAiProperties.ModelPrice price = properties.getPricing().get(line.model());
                    if (price == null) {
                        warnings.add("No price is configured for %s, so %d Almo turns are shown without dollars"
                                .formatted(line.model(), line.turns()));
                    }
                    return new EstimatedLine(
                            ALMO,
                            "chat",
                            line.model(),
                            line.turns(),
                            line.promptTokens(),
                            0,
                            line.completionTokens(),
                            price == null ? null : priced(price, line.promptTokens(), line.completionTokens()));
                })
                .toList();
    }

    private List<EstimatedLine> booksLines(Instant since, Instant until, List<String> warnings) {
        try {
            return bookProcessor.aiSpend(since, until).stream()
                    .map(line -> new EstimatedLine(
                            BOOKS,
                            line.purpose(),
                            line.model(),
                            line.runs(),
                            line.inputTokens(),
                            line.cachedInputTokens(),
                            line.outputTokens(),
                            line.estimatedCostUsd()))
                    .toList();
        } catch (ApiIntegrationException e) {
            log.warn("The book processor's spend could not be read", e);
            warnings.add("The book processor could not be reached, so book spend is missing");
            return List.of();
        }
    }

    /** One fetch per window start serves the page for the configured hour; a failed refresh keeps the last one. */
    private FetchedCosts actualCosts(LocalDate firstDay, Instant since, int days, List<String> warnings) {
        FetchedCosts cached = costsByWindowStart.get(firstDay);
        Instant now = Instant.now();
        if (cached != null
                && cached.fetchedAt().plus(properties.getCostsCacheTtl()).isAfter(now)) {
            return cached;
        }
        try {
            List<ActualLine> lines = costsClient.dailyCosts(since, days).stream()
                    .map(cost -> new ActualLine(cost.day(), cost.projectId(), cost.lineItem(), cost.usd()))
                    .toList();
            FetchedCosts fresh = new FetchedCosts(now, lines);
            costsByWindowStart.put(firstDay, fresh);
            return fresh;
        } catch (ApiIntegrationException e) {
            log.warn("OpenAI's costs could not be read", e);
            warnings.add(
                    cached == null
                            ? "OpenAI's bill could not be read, so the actual side is missing"
                            : "OpenAI's bill could not be refreshed; the actual side is from " + cached.fetchedAt());
            return cached;
        }
    }

    static BigDecimal priced(OpenAiProperties.ModelPrice price, long inputTokens, long outputTokens) {
        return BigDecimal.valueOf(inputTokens)
                .multiply(price.getInput())
                .add(BigDecimal.valueOf(outputTokens).multiply(price.getOutput()))
                .divide(TOKENS_PER_PRICE_UNIT, USD_SCALE, RoundingMode.HALF_UP);
    }
}
