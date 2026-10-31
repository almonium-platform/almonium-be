package com.almonium.analyzer.analyzer.dto;

import java.util.List;
import java.util.UUID;

/**
 * One sense on the Discover sheet. {@code definition}, {@code cefr} and {@code examples} are filled only when the
 * sense came from the lexicon; the old providers leave them null and empty. {@code key} is the lexicon sense's
 * identity across rewrites, which a card saved from it keeps; null from the old providers.
 */
public record DiscoverSenseResponse(
        int index,
        UUID key,
        String headword,
        String partOfSpeech,
        String transcription,
        List<String> translations,
        String definition,
        String cefr,
        List<DiscoverExampleResponse> examples) {}
