package com.almonium.learning.lexicon.dto.request;

import com.almonium.analyzer.translator.model.enums.Language;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Two dictionary senses to explain the difference between, by their keys: the facts are read from the lexicon, never
 * taken from the client. {@code explanationLanguage} is the language the explanation is written in. With
 * {@code generate} false only an explanation already bought is returned, which costs nothing.
 */
public record SenseComparisonRequest(
        @NotNull UUID firstSenseKey,
        @NotNull UUID secondSenseKey,
        @NotNull Language explanationLanguage,
        boolean generate) {}
