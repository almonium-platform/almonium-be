package com.almonium.analyzer.analyzer.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.analyzer.analyzer.dto.IpaSegmentResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TranscriptionDiffTest {

    private static List<List<String>> tinted(String language, String... transcriptions) {
        return TranscriptionDiff.segments(Arrays.asList(transcriptions), language).stream()
                .map(line -> line.stream()
                        .filter(IpaSegmentResponse::differs)
                        .map(IpaSegmentResponse::text)
                        .toList())
                .toList();
    }

    @Test
    void cutsEveryTranscriptionIntoSegmentsThatConcatenateToIt() {
        List<List<IpaSegmentResponse>> segments = TranscriptionDiff.segments(List.of("/ˈklɪə/", "/ˈklɪɹ/"), "en");
        assertThat(segments.get(0))
                .containsExactly(
                        new IpaSegmentResponse("/ˈklɪ", false),
                        new IpaSegmentResponse("ə", true),
                        new IpaSegmentResponse("/", false));
        assertThat(String.join(
                        "",
                        segments.get(1).stream().map(IpaSegmentResponse::text).toList()))
                .isEqualTo("/ˈklɪɹ/");
    }

    @Test
    void comparesADiphthongWholeAndReadsɹAsr() {
        assertThat(tinted("en", "/nəʊˈtɔːriəs/", "/noʊˈtɔːɹiəs/")).containsExactly(List.of("əʊ"), List.of("oʊ"));
    }

    @Test
    void tintsAnInsertedSoundOnlyInTheLineThatHasIt() {
        assertThat(tinted("en", "/ˈkɑː/", "/ˈkɑːɹ/")).containsExactly(List.of(), List.of("ɹ"));
    }

    @Test
    void readsARhoticVowelAsVowelAndR() {
        assertThat(tinted("en", "/ˈbɜːrd/", "/ˈbɝd/")).containsExactly(List.of(), List.of());
    }

    @Test
    void comparesStressWhenBothMarkItAndIgnoresItWhenOneDoesNot() {
        assertThat(tinted("en", "/ɡəˈrɑːʒ/", "/ˈɡærɑːʒ/")).containsExactly(List.of("əˈ"), List.of("ˈ", "æ"));
        assertThat(tinted("en", "/bləʊk/", "/ˈbləʊk/")).containsExactly(List.of(), List.of());
    }

    @Test
    void ignoresSyllableDots() {
        assertThat(tinted("en", "/ˈwɔː.tə/", "/ˈwɔːtə/")).containsExactly(List.of(), List.of());
    }

    @Test
    void keepsMarksOnTheirSymbolAndJoinsANonSyllabicVowelInAnyLanguage() {
        assertThat(TranscriptionDiff.phonemes("t͡ʃiːz", Set.of())).containsExactly("t͡ʃ", "iː", "z");
        assertThat(TranscriptionDiff.phonemes("haɪ̯s", Set.of())).containsExactly("h", "aɪ̯", "s");
    }

    @Test
    void comparesSymbolBySymbolInALanguageWithoutAList() {
        assertThat(tinted("xx", "noʊ", "nəʊ")).containsExactly(List.of("o"), List.of("ə"));
    }

    @Test
    void leavesAMissingTranscriptionOut() {
        List<List<IpaSegmentResponse>> segments = TranscriptionDiff.segments(Arrays.asList("/a/", null), "en");
        assertThat(segments.get(0)).containsExactly(new IpaSegmentResponse("/a/", false));
        assertThat(segments.get(1)).isNull();
    }
}
