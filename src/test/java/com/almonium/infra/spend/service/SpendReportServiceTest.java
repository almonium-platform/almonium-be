package com.almonium.infra.spend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.client.exception.ApiIntegrationException;
import com.almonium.config.properties.OpenAiProperties;
import com.almonium.infra.spend.client.OpenAiCostsClient;
import com.almonium.infra.spend.dto.SpendReport;
import com.almonium.learning.almo.dto.AlmoSpendLine;
import com.almonium.learning.almo.repository.AlmoTurnRepository;
import com.almonium.learning.book.dto.response.BooksSpendLine;
import com.almonium.learning.book.service.BookProcessorClient;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SpendReportServiceTest {
    @Mock
    private AlmoTurnRepository turnRepository;

    @Mock
    private BookProcessorClient bookProcessor;

    @Mock
    private OpenAiCostsClient costsClient;

    private OpenAiProperties properties;
    private SpendReportService service;

    @BeforeEach
    void setUp() {
        properties = new OpenAiProperties();
        OpenAiProperties.ModelPrice mini = new OpenAiProperties.ModelPrice();
        mini.setInput(new BigDecimal("0.40"));
        mini.setCachedInput(new BigDecimal("0.10"));
        mini.setOutput(new BigDecimal("1.60"));
        properties.getPricing().put("gpt-4.1-mini", mini);
        service = new SpendReportService(turnRepository, bookProcessor, costsClient, properties);
    }

    @Test
    void pricesAlmoFromTheTableAndTakesBookDollarsAsGiven() {
        when(turnRepository.spendBetween(any(), any()))
                .thenReturn(List.of(new AlmoSpendLine("gpt-4.1-mini", 3, 1_000_000, 250_000)));
        when(bookProcessor.aiSpend(any(), any()))
                .thenReturn(List.of(new BooksSpendLine(
                        "translation", "gpt-5.6-terra", 2, 500, 100, 200, 0, new BigDecimal("0.550000"))));
        when(costsClient.dailyCosts(any(), anyInt()))
                .thenReturn(List.of(
                        new OpenAiCostsClient.DailyCost(
                                LocalDate.of(2026, 9, 5), "proj_a", "gpt-4.1-mini, input", new BigDecimal("0.4")),
                        new OpenAiCostsClient.DailyCost(
                                LocalDate.of(2026, 9, 6), "proj_a", "gpt-4.1-mini, output", new BigDecimal("0.5"))));

        SpendReport report = service.report(7);

        assertThat(report.estimated())
                .containsExactly(
                        new SpendReport.EstimatedLine(
                                "almo", "chat", "gpt-4.1-mini", 3, 1_000_000, 0, 250_000, new BigDecimal("0.800000")),
                        new SpendReport.EstimatedLine(
                                "books", "translation", "gpt-5.6-terra", 2, 500, 100, 200, new BigDecimal("0.550000")));
        assertThat(report.estimatedUsd()).isEqualByComparingTo("1.35");
        assertThat(report.actual()).hasSize(2);
        assertThat(report.actualUsd()).isEqualByComparingTo("0.9");
        assertThat(report.actualFetchedAt()).isNotNull();
        assertThat(report.warnings()).isEmpty();
        assertThat(Duration.between(report.since(), report.until())).isGreaterThan(Duration.ofDays(6));
    }

    @Test
    void anUnpricedModelKeepsItsTokensAndSaysSo() {
        when(turnRepository.spendBetween(any(), any())).thenReturn(List.of(new AlmoSpendLine("gpt-6", 4, 10, 5)));
        when(bookProcessor.aiSpend(any(), any())).thenReturn(List.of());
        when(costsClient.dailyCosts(any(), anyInt())).thenReturn(List.of());

        SpendReport report = service.report(1);

        assertThat(report.estimated()).singleElement().satisfies(line -> {
            assertThat(line.estimatedUsd()).isNull();
            assertThat(line.inputTokens()).isEqualTo(10);
        });
        assertThat(report.estimatedUsd()).isEqualByComparingTo("0");
        assertThat(report.warnings())
                .singleElement()
                .asString()
                .contains("gpt-6")
                .contains("4 Almo turns");
    }

    @Test
    void unreachableUpstreamsLeaveTheirSideEmptyWithAWarning() {
        when(turnRepository.spendBetween(any(), any())).thenReturn(List.of());
        when(bookProcessor.aiSpend(any(), any())).thenThrow(new ApiIntegrationException("down"));
        when(costsClient.dailyCosts(any(), anyInt())).thenThrow(new ApiIntegrationException("down"));

        SpendReport report = service.report(30);

        assertThat(report.estimated()).isEmpty();
        assertThat(report.actual()).isEmpty();
        assertThat(report.actualFetchedAt()).isNull();
        assertThat(report.warnings()).hasSize(2);
    }

    @Test
    void theBillIsFetchedOncePerWindowWhileTheCacheHolds() {
        when(turnRepository.spendBetween(any(), any())).thenReturn(List.of());
        when(bookProcessor.aiSpend(any(), any())).thenReturn(List.of());
        when(costsClient.dailyCosts(any(), anyInt())).thenReturn(List.of());

        Instant firstFetch = service.report(30).actualFetchedAt();
        service.report(30);
        service.report(7);

        verify(costsClient, times(2)).dailyCosts(any(), anyInt());
        assertThat(firstFetch).isNotNull();
    }
}
