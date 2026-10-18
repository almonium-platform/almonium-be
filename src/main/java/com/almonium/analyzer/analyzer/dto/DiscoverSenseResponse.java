package com.almonium.analyzer.analyzer.dto;

import java.util.List;

/**
 * One sense on the Discover sheet. {@code definition}, {@code cefr} and {@code examples} are filled only when the
 * sense came from the lexicon; the old providers leave them null and empty.
 */
public record DiscoverSenseResponse(
        int index,
        String headword,
        String partOfSpeech,
        String transcription,
        List<String> translations,
        String definition,
        String cefr,
        List<DiscoverExampleResponse> examples) {}
