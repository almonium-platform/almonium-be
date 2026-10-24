package com.almonium.analyzer.analyzer.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.analyzer.analyzer.dto.IpaSegmentResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TranscriptionDiffTest {

    /** The tinted runs of each line, in order. */
    private static List<List<String>> tinted(String language, String... transcriptions) {
        return TranscriptionDiff.segments(Arrays.asList(transcriptions), language).stream()
                .map(line -> line == null
                        ? null
                        : line.stream()
                                .filter(IpaSegmentResponse::differs)
                                .map(IpaSegmentResponse::text)
                                .toList())
                .toList();
    }

    private static String joined(List<IpaSegmentResponse> segments) {
        return String.join("", segments.stream().map(IpaSegmentResponse::text).toList());
    }

    @Nested
    class Segments {
        @Test
        void cutTheTranscriptionAtEachChangeOfState() {
            assertThat(TranscriptionDiff.segments(List.of("/ˈklɪə/", "/ˈklɪɹ/"), "en")
                            .get(0))
                    .containsExactly(
                            new IpaSegmentResponse("/ˈklɪ", false),
                            new IpaSegmentResponse("ə", true),
                            new IpaSegmentResponse("/", false));
        }

        @Test
        void mergeNeighbouringDifferencesIntoOneRun() {
            assertThat(tinted("en", "/təˈmɑːtəʊ/", "/təˈmeɪɾoʊ/")).containsExactly(List.of("ɑːtəʊ"), List.of("eɪɾoʊ"));
        }

        @Test
        void leaveTheDelimitersUntintedWhicheverTheyAre() {
            List<List<IpaSegmentResponse>> segments = TranscriptionDiff.segments(List.of("[ˈklɪə]", "/ˈklɪɹ/"), "en");
            assertThat(segments.get(0).getFirst()).isEqualTo(new IpaSegmentResponse("[ˈklɪ", false));
            assertThat(segments.get(0).getLast()).isEqualTo(new IpaSegmentResponse("]", false));
        }

        @Test
        void workWithoutDelimiters() {
            assertThat(tinted("en", "ˈklɪə", "ˈklɪɹ")).containsExactly(List.of("ə"), List.of("ɹ"));
        }

        @ParameterizedTest
        @MethodSource("com.almonium.analyzer.analyzer.mapper.TranscriptionDiffTest#awkwardPairs")
        void alwaysConcatenateToTheTranscriptionExactly(String first, String second) {
            List<List<IpaSegmentResponse>> segments = TranscriptionDiff.segments(List.of(first, second), "en");
            assertThat(joined(segments.get(0))).isEqualTo(first);
            assertThat(joined(segments.get(1))).isEqualTo(second);
        }

        @ParameterizedTest
        @MethodSource("com.almonium.analyzer.analyzer.mapper.TranscriptionDiffTest#awkwardPairs")
        void neverPutTwoRunsOfTheSameStateSideBySide(String first, String second) {
            for (List<IpaSegmentResponse> line : TranscriptionDiff.segments(List.of(first, second), "en")) {
                for (int index = 1; index < line.size(); index++) {
                    assertThat(line.get(index).differs())
                            .isNotEqualTo(line.get(index - 1).differs());
                }
            }
        }
    }

    @Nested
    class Phonemes {
        @Test
        void keepLengthDiacriticsAndATiedPairOnTheirSymbol() {
            assertThat(TranscriptionDiff.phonemes("t͡ʃiːz", Set.of())).containsExactly("t͡ʃ", "iː", "z");
            assertThat(TranscriptionDiff.phonemes("ˈbʌtn̩", Set.of())).containsExactly("ˈ", "b", "ʌ", "t", "n̩");
            assertThat(TranscriptionDiff.phonemes("pʰɪn", Set.of())).containsExactly("pʰ", "ɪ", "n");
            assertThat(TranscriptionDiff.phonemes("bɑ̃", Set.of())).containsExactly("b", "ɑ̃");
        }

        @Test
        void joinTheLanguagesTwoSymbolSounds() {
            Set<String> english = Set.of("oʊ", "tʃ");
            assertThat(TranscriptionDiff.phonemes("tʃoʊk", english)).containsExactly("tʃ", "oʊ", "k");
            assertThat(TranscriptionDiff.phonemes("tʃoʊk", Set.of())).containsExactly("t", "ʃ", "o", "ʊ", "k");
        }

        @Test
        void joinANonSyllabicVowelToTheOneBeforeItInAnyLanguage() {
            assertThat(TranscriptionDiff.phonemes("haɪ̯s", Set.of())).containsExactly("h", "aɪ̯", "s");
        }

        @Test
        void neverAttachAMarkToADelimiterOrASpace() {
            assertThat(TranscriptionDiff.phonemes("/ː", Set.of())).containsExactly("/", "ː");
            assertThat(TranscriptionDiff.phonemes("a ̃b", Set.of())).containsExactly("a", " ", "̃", "b");
        }

        @Test
        void survivePiecesThatHaveNothingToAttachTo() {
            assertThat(TranscriptionDiff.phonemes("̃a", Set.of())).containsExactly("̃", "a");
            assertThat(TranscriptionDiff.phonemes("t͡", Set.of())).containsExactly("t͡");
            assertThat(TranscriptionDiff.phonemes("", Set.of())).isEmpty();
        }
    }

    @Nested
    class Alignment {
        @Test
        void tintsNothingWhenTheTranscriptionsAreTheSame() {
            assertThat(tinted("en", "/kæt/", "/kæt/")).containsExactly(List.of(), List.of());
        }

        @Test
        void tintsASubstitutedSoundInBothLines() {
            assertThat(tinted("en", "/bɑːθ/", "/bæθ/")).containsExactly(List.of("ɑː"), List.of("æ"));
        }

        @Test
        void tintsAnInsertedSoundOnlyInTheLineThatHasIt() {
            assertThat(tinted("en", "/ˈkɑː/", "/ˈkɑːɹ/")).containsExactly(List.of(), List.of("ɹ"));
            assertThat(tinted("en", "/æk/", "/hæk/")).containsExactly(List.of(), List.of("h"));
        }

        @Test
        void comparesADiphthongWhole() {
            assertThat(tinted("en", "/nəʊˈtɔːriəs/", "/noʊˈtɔːɹiəs/")).containsExactly(List.of("əʊ"), List.of("oʊ"));
            assertThat(tinted("en", "/ɡəʊt/", "/ɡoʊt/")).containsExactly(List.of("əʊ"), List.of("oʊ"));
        }

        @Test
        void keepsACentringDiphthongAsTwoSoTheSharedVowelStaysPlain() {
            assertThat(tinted("en", "/ˈklɪə/", "/ˈklɪɹ/")).containsExactly(List.of("ə"), List.of("ɹ"));
        }

        @Test
        void comparesALongVowelWithTheShortOneAsDifferent() {
            assertThat(tinted("en", "/biːt/", "/bit/")).containsExactly(List.of("iː"), List.of("i"));
        }

        @Test
        void neverTradesASharedSoundToShiftARun() {
            assertThat(tinted("en", "/ɡəˈrɑːʒ/", "/ˈɡærɑːʒ/")).containsExactly(List.of("əˈ"), List.of("ˈ", "æ"));
        }

        @Test
        void tintsSeveralSeparateDifferences() {
            assertThat(tinted("en", "/ˈwɔːtə/", "/ˈwɑːɾɚ/")).containsExactly(List.of("ɔːt"), List.of("ɑːɾɚ"));
        }

        @Test
        void tintsEverythingWhenNothingIsShared() {
            assertThat(tinted("en", "/aɪ/", "/oʊ/")).containsExactly(List.of("aɪ"), List.of("oʊ"));
        }

        @Test
        void comparesTwoWordsOfAPhraseAcrossTheSpace() {
            assertThat(tinted("en", "/lʊk ʌp/", "/lʊk ʌp/")).containsExactly(List.of(), List.of());
            assertThat(tinted("en", "/ɡəʊ ɒn/", "/ɡoʊ ɑn/")).containsExactly(List.of("əʊ", "ɒ"), List.of("oʊ", "ɑ"));
        }
    }

    @Nested
    class Notation {
        @Test
        void readsɹAndrAsOneSound() {
            assertThat(tinted("en", "/ɹiːd/", "/riːd/")).containsExactly(List.of(), List.of());
        }

        @Test
        void readsTheTwoLettersGAsOne() {
            assertThat(tinted("en", "/ɡeɪm/", "/geɪm/")).containsExactly(List.of(), List.of());
        }

        @Test
        void readsARhoticVowelAsVowelAndR() {
            assertThat(tinted("en", "/bɜːrd/", "/bɝd/")).containsExactly(List.of(), List.of());
            assertThat(tinted("en", "/ˈlɛtər/", "/ˈlɛtɚ/")).containsExactly(List.of(), List.of());
        }

        @Test
        void tintsOnlyTheRhoticVowelAgainstANonRhoticOne() {
            assertThat(tinted("en", "/bɜːd/", "/bɝd/")).containsExactly(List.of(), List.of("ɝ"));
        }

        @Test
        void readsATiedAffricateAsTheUntiedOne() {
            assertThat(tinted("en", "/t͡ʃɜːtʃ/", "/tʃɜːt͡ʃ/")).containsExactly(List.of(), List.of());
        }

        @Test
        void readsANonSyllabicMarkAsNotation() {
            assertThat(tinted("de", "/haɪ̯s/", "/haɪs/")).containsExactly(List.of(), List.of());
        }

        @Test
        void ignoresSyllableDots() {
            assertThat(tinted("en", "/ˈwɔː.tə/", "/ˈwɔːtə/")).containsExactly(List.of(), List.of());
        }

        @Test
        void keepsATapAsARealDifference() {
            assertThat(tinted("en", "/ˈbʌtə/", "/ˈbʌɾə/")).containsExactly(List.of("t"), List.of("ɾ"));
        }
    }

    @Nested
    class Stress {
        @Test
        void comparesItWhenBothLinesMarkIt() {
            assertThat(tinted("en", "/ˈædres/", "/əˈdres/")).containsExactly(List.of("ˈæ"), List.of("əˈ"));
        }

        @Test
        void tellsPrimaryFromSecondary() {
            assertThat(tinted("en", "/ˌʌndəˈstænd/", "/ˈʌndəˌstænd/"))
                    .containsExactly(List.of("ˌ", "ˈ"), List.of("ˈ", "ˌ"));
        }

        @Test
        void ignoresItWhenOneLineMarksNone() {
            assertThat(tinted("en", "/bləʊk/", "/ˈbləʊk/")).containsExactly(List.of(), List.of());
            assertThat(tinted("en", "/bləʊk/", "/ˈbloʊk/")).containsExactly(List.of("əʊ"), List.of("oʊ"));
        }

        @Test
        void decidesItPerPairAmongThreeLines() {
            assertThat(tinted("en", "/ˈædres/", "/ədres/", "/əˈdres/"))
                    .containsExactly(List.of("ˈæ"), List.of("ə"), List.of("əˈ"));
        }
    }

    @Nested
    class Languages {
        @Test
        void readTheLanguageCodeInAnyCase() {
            assertThat(tinted("EN", "/ɡəʊt/", "/ɡoʊt/")).containsExactly(List.of("əʊ"), List.of("oʊ"));
        }

        @Test
        void compareSymbolBySymbolWithoutAList() {
            assertThat(tinted("xx", "noʊ", "nəʊ")).containsExactly(List.of("o"), List.of("ə"));
            assertThat(tinted(null, "noʊ", "nəʊ")).containsExactly(List.of("o"), List.of("ə"));
        }

        @Test
        void useGermanDiphthongsAndAffricates() {
            assertThat(tinted("de", "/tsaɪt/", "/tsɔʏt/")).containsExactly(List.of("aɪ"), List.of("ɔʏ"));
            assertThat(tinted("de", "/pfɛrt/", "/fɛrt/")).containsExactly(List.of("pf"), List.of("f"));
        }
    }

    @Nested
    class SeveralLines {
        @Test
        void tintTheVariantDifferenceWhenBothAccentsRecordTheSameTwo() {
            assertThat(tinted("en", "/ˈniːʃ/", "/ˈnɪtʃ/", "/ˈnɪtʃ/", "/ˈniːʃ/"))
                    .containsExactly(List.of("iːʃ"), List.of("ɪtʃ"), List.of("ɪtʃ"), List.of("iːʃ"));
        }

        @Test
        void tintNothingWhenEveryAccentRecordsTheSameTranscription() {
            assertThat(tinted("en", "/bʊk/", "/bʊk/")).containsExactly(List.of(), List.of());
        }

        @Test
        void tintAPhonemeThatDiffersFromAnyOtherLine() {
            assertThat(tinted("en", "/ˈklɪə/", "/ˈklɪɹ/", "/ˈklɪə/"))
                    .containsExactly(List.of("ə"), List.of("ɹ"), List.of("ə"));
        }

        @Test
        void doNotDependOnTheOrderOfTheLines() {
            List<String> lines = List.of("/ˈdɑːns/", "/ˈdæns/", "/ˈdæːns/");
            List<String> reversed = new ArrayList<>(lines);
            java.util.Collections.reverse(reversed);
            List<List<IpaSegmentResponse>> forward = TranscriptionDiff.segments(lines, "en");
            List<List<IpaSegmentResponse>> backward = new ArrayList<>(TranscriptionDiff.segments(reversed, "en"));
            java.util.Collections.reverse(backward);
            assertThat(backward).isEqualTo(forward);
        }

        @Test
        void leaveAMissingTranscriptionOutOfTheComparison() {
            List<List<IpaSegmentResponse>> segments =
                    TranscriptionDiff.segments(Arrays.asList("/a/", null, "/a/"), "en");
            assertThat(segments.get(0)).containsExactly(new IpaSegmentResponse("/a/", false));
            assertThat(segments.get(1)).isNull();
        }

        @Test
        void handleNoLinesAndOneLine() {
            assertThat(TranscriptionDiff.segments(List.of(), "en")).isEmpty();
            assertThat(TranscriptionDiff.segments(List.of("/kæt/"), "en").getFirst())
                    .containsExactly(new IpaSegmentResponse("/kæt/", false));
        }

        @Test
        void handleAnEmptyTranscription() {
            List<List<IpaSegmentResponse>> segments = TranscriptionDiff.segments(List.of("", "/kæt/"), "en");
            assertThat(segments.get(0)).isEmpty();
            assertThat(segments.get(1))
                    .containsExactly(
                            new IpaSegmentResponse("/", false),
                            new IpaSegmentResponse("kæt", true),
                            new IpaSegmentResponse("/", false));
        }
    }

    static Stream<Arguments> awkwardPairs() {
        return Stream.of(
                Arguments.of("/ˈklɪə/", "/ˈklɪɹ/"),
                Arguments.of("[t͡ʃiːz]", "tʃiz"),
                Arguments.of("/ˈwɔː.tə/", "/ˈwɑːɾɚ/"),
                Arguments.of("̃a", "a͡"),
                Arguments.of("", "/ /"),
                Arguments.of("/haɪ̯s/", "/ˌhaɪs ˈhaʊs/"),
                Arguments.of("/ɡəˈrɑːʒ/", "/ˈɡærɑːʒ/"),
                Arguments.of("//", "[]"));
    }
}
