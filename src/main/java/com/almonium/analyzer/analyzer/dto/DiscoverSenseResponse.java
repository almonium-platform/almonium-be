package com.almonium.analyzer.analyzer.dto;

import com.almonium.learning.lexicon.model.RegionScope;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * One sense on the Discover sheet. Everything from {@code definition} on is filled only when the sense came from
 * the lexicon; the old providers leave it null and empty. {@code key} is the lexicon sense's identity across
 * rewrites, which a card saved from it keeps; null from the old providers. {@code lexeme} is the
 * {@code DiscoverLexemeResponse.index} of the word the sense belongs to. {@code labels} says what kind of word this
 * is here, each axis by itself (formality, attitude, time, style, normativity, domain, figurative, rare,
 * politeness); {@code regions} where it is used when not everywhere; {@code grammar} what differs from the word's
 * own grammar in this sense. {@code frames} are how the word is built into a sentence (decide on sth),
 * {@code collocations} the words it is commonly said with, {@code antonyms} its opposites, and
 * {@code standardAlternative} the standard word for a nonstandard one.
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
        List<DiscoverExampleResponse> examples,
        Integer lexeme,
        Map<String, Object> labels,
        List<RegionScope> regions,
        Map<String, Object> grammar,
        List<String> frames,
        List<String> collocations,
        List<String> antonyms,
        String standardAlternative) {

    /** A sense as a translation provider gives it: no lexicon behind it. */
    public DiscoverSenseResponse(
            int index,
            UUID key,
            String headword,
            String partOfSpeech,
            String transcription,
            List<String> translations,
            String definition,
            String cefr,
            List<DiscoverExampleResponse> examples) {
        this(
                index,
                key,
                headword,
                partOfSpeech,
                transcription,
                translations,
                definition,
                cefr,
                examples,
                null,
                Map.of(),
                List.of(),
                Map.of(),
                List.of(),
                List.of(),
                List.of(),
                null);
    }
}
