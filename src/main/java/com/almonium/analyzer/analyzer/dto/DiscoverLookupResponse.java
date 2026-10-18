package com.almonium.analyzer.analyzer.dto;

import com.almonium.analyzer.translator.model.enums.Language;
import java.util.List;

/**
 * The Discover sheet for one word. {@code provider} is {@code lexicon} when the lexicon had the word, otherwise the
 * old translation provider's name; {@code pronunciations} is empty in the latter case.
 */
public record DiscoverLookupResponse(
        String entry,
        String sourceContext,
        Language language,
        Language translationLanguage,
        String provider,
        DiscoverFrequencyResponse frequency,
        List<DiscoverSenseResponse> senses,
        List<DiscoverPronunciationResponse> pronunciations) {}
