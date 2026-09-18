package com.almonium.learning.book.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.learning.book.dto.response.ChapterVocabulary;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

/** Rarest band first, each lemma once, met order within a band, the form the reader saw. */
class RarestWordsSelectorTest {
    private final RarestWordsSelector selector = new RarestWordsSelector();

    @Test
    void rarestBandsComeFirstAndALemmaIsCountedOnce() {
        ChapterVocabulary one = chapter(1, word("hum", "hummed", "common"), word("heffalump", "Heffalump", "rare"));
        ChapterVocabulary two =
                chapter(2, word("Heffalump", "Heffalumps", "rare"), word("stout", "stoutness", "uncommon"));

        assertThat(selector.rarest(List.of(one, two))).containsExactly("Heffalump", "stoutness", "hummed");
    }

    @Test
    void twelveIsTheCeilingAndAnUnnamedBandRanksLast() {
        ChapterVocabulary.Word[] many = IntStream.range(0, 15)
                .mapToObj(i -> word("w" + i, "w" + i, i == 0 ? null : "rare"))
                .toArray(ChapterVocabulary.Word[]::new);

        List<String> words = selector.rarest(List.of(chapter(1, many)));

        assertThat(words).hasSize(12).startsWith("w1").doesNotContain("w0");
    }

    @Test
    void aChapterWithoutWordsContributesNothing() {
        ChapterVocabulary pending = new ChapterVocabulary(UUID.randomUUID(), 3, "en", "pending", "curated", null, null);
        assertThat(selector.rarest(List.of(pending))).isEmpty();
    }

    private static ChapterVocabulary chapter(int sequence, ChapterVocabulary.Word... words) {
        return new ChapterVocabulary(UUID.randomUUID(), sequence, "en", "ready", "curated", List.of(words), null);
    }

    private static ChapterVocabulary.Word word(String lemma, String surface, String band) {
        return new ChapterVocabulary.Word(lemma, surface, "context", "c1.p1", 0, 1, band);
    }
}
