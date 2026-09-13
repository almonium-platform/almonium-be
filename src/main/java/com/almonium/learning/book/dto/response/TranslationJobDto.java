package com.almonium.learning.book.dto.response;

import com.almonium.learning.book.model.enums.TranslationJobPhase;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TranslationJobDto(
        UUID id,
        TranslationJobPhase phase,
        Integer progressCompleted,
        Integer progressTotal,
        BigDecimal estimatedCostUsd,
        BigDecimal actualCostUsd,
        String tier,
        String mode,
        String processorEditionSlug,
        Instant approvedAt,
        Instant finishedAt,
        String error,
        UUID publishedBookId,
        String publishedEditionSlug) {}
