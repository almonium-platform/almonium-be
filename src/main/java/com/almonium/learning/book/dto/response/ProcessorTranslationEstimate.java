package com.almonium.learning.book.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

/** What the processor says a translation would cost, before anyone is charged. */
public record ProcessorTranslationEstimate(
        UUID editionId, String editionSlug, int chapters, int blocks, BigDecimal estimatedCostUsd) {}
