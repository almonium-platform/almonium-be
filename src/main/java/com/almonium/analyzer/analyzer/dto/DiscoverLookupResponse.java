package com.almonium.analyzer.analyzer.dto;

import com.almonium.analyzer.translator.model.enums.Language;
import java.util.List;

/**
 * The Discover sheet for one word. {@code provider} is {@code lexicon} when the lexicon had the word, otherwise the
 * old translation provider's name; {@code pronunciations}, {@code homophones} and {@code lexemes} (the words the
 * senses belong to) are empty in the latter case. When a standalone lookup misses {@code requestedLanguage} and hits
 * another language the reader can open, {@code language} is the matched language and {@code requestedLanguage} names
 * the one originally asked for; {@code availableLanguages} lists other lexicon languages that also hold the word.
 */
public record DiscoverLookupResponse(
        String entry,
        String sourceContext,
        Language language,
        Language translationLanguage,
        String provider,
        DiscoverFrequencyResponse frequency,
        List<DiscoverSenseResponse> senses,
        List<DiscoverPronunciationResponse> pronunciations,
        List<DiscoverHomophoneResponse> homophones,
        List<DiscoverLexemeResponse> lexemes,
        Language requestedLanguage,
        List<Language> availableLanguages,
        boolean scriptMismatch) {

    public DiscoverLookupResponse(
            String entry,
            String sourceContext,
            Language language,
            Language translationLanguage,
            String provider,
            DiscoverFrequencyResponse frequency,
            List<DiscoverSenseResponse> senses,
            List<DiscoverPronunciationResponse> pronunciations,
            List<DiscoverHomophoneResponse> homophones,
            List<DiscoverLexemeResponse> lexemes) {
        this(
                entry,
                sourceContext,
                language,
                translationLanguage,
                provider,
                frequency,
                senses,
                pronunciations,
                homophones,
                lexemes,
                null,
                List.of(),
                false);
    }

    /** A sheet no lexicon entry stands behind. */
    public DiscoverLookupResponse(
            String entry,
            String sourceContext,
            Language language,
            Language translationLanguage,
            String provider,
            DiscoverFrequencyResponse frequency,
            List<DiscoverSenseResponse> senses,
            List<DiscoverPronunciationResponse> pronunciations,
            List<DiscoverHomophoneResponse> homophones) {
        this(
                entry,
                sourceContext,
                language,
                translationLanguage,
                provider,
                frequency,
                senses,
                pronunciations,
                homophones,
                List.of(),
                null,
                List.of(),
                false);
    }
}
