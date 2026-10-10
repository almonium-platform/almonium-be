package com.almonium.learning.wanted.dto.request;

import com.almonium.analyzer.translator.model.enums.Language;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/** The factory's claim on a wanted work: the work, the language it is wanted in, and the free text it will fetch. */
public record FactoryReservationRequest(
        @NotNull UUID catalogWorkId,
        @NotNull Language language,
        @NotNull UUID sourceId) {}
