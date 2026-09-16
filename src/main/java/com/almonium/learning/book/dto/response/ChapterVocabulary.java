package com.almonium.learning.book.dto.response;

import java.util.List;
import java.util.UUID;

/** Curated attested examples, not an exhaustive chapter dictionary or CEFR word list. */
public record ChapterVocabulary(
        UUID chapterId,
        int chapterSequence,
        String language,
        String status,
        String selection,
        List<Word> words,
        Provenance provenance) {
    public record Word(
            String lemma, String surface, String context, String blockId, int start, int end, String frequencyBand) {}

    public record Provenance(String inputHash, String processorVersion, String spacyModel, String spacyModelVersion) {}
}
