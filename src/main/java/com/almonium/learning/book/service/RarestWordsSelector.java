package com.almonium.learning.book.service;

import com.almonium.learning.book.dto.response.ChapterVocabulary;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * The twelve rarest words a reader met in a book (design K): the processor's curated words from every chapter,
 * rarest band first, each word once, in the order they were met within a band. The form shown is the one the
 * reader actually saw, not the dictionary lemma, because it is the meeting that is being remembered.
 */
@Component
public class RarestWordsSelector {
    public static final int COUNT = 12;

    /** Rarest first; a band the processor has not named ranks after the ones it has. */
    private static final List<String> BANDS = List.of("rare", "uncommon", "common", "very_common");

    public List<String> rarest(List<ChapterVocabulary> chapters) {
        Map<String, Ranked> byLemma = new LinkedHashMap<>();
        int order = 0;
        for (ChapterVocabulary chapter : chapters) {
            if (chapter.words() == null) continue;
            for (ChapterVocabulary.Word word : chapter.words()) {
                String lemma = key(word);
                if (lemma.isEmpty() || byLemma.containsKey(lemma)) continue;
                String shown = word.surface() == null || word.surface().isBlank() ? word.lemma() : word.surface();
                byLemma.put(lemma, new Ranked(shown.trim(), rank(word.frequencyBand()), order++));
            }
        }
        List<Ranked> ranked = new ArrayList<>(byLemma.values());
        ranked.sort((a, b) -> a.band != b.band ? Integer.compare(a.band, b.band) : Integer.compare(a.met, b.met));
        return ranked.stream().limit(COUNT).map(Ranked::shown).toList();
    }

    private static String key(ChapterVocabulary.Word word) {
        String base = word.lemma() == null || word.lemma().isBlank() ? word.surface() : word.lemma();
        return base == null ? "" : base.trim().toLowerCase(Locale.ROOT);
    }

    private static int rank(String band) {
        int index = band == null ? -1 : BANDS.indexOf(band.trim().toLowerCase(Locale.ROOT));
        return index < 0 ? BANDS.size() : index;
    }

    private record Ranked(String shown, int band, int met) {}
}
