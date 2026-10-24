package com.almonium.analyzer.analyzer.mapper;

import com.almonium.analyzer.analyzer.dto.DiscoverExampleResponse;
import com.almonium.analyzer.analyzer.dto.DiscoverPronunciationResponse;
import com.almonium.analyzer.analyzer.dto.DiscoverSenseResponse;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.ClipView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.ExampleView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.GlossView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.LexemeView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.PronunciationView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.SenseView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

/**
 * Flattens a lexicon entry onto the Discover sheet, which knows one list of senses and one of pronunciations and
 * nothing of lexemes. Senses are numbered across lexemes in order; pronunciations are the headword-level ones, by
 * variety then rank, each carrying its best clip (a verified one before an unverified one, which is the order the
 * lexicon already lists them in). A word with several lexemes (clear as adjective, verb and adverb) records the same
 * pronunciation on each, so one variety saying the same IPA is shown once, with the best clip any lexeme has.
 */
@Component
public class DiscoverLexiconMapper {
    public static final String PROVIDER = "lexicon";

    private static final Comparator<GlossView> EXACT_FIRST =
            Comparator.comparing((GlossView gloss) -> !Boolean.TRUE.equals(gloss.exact()));
    private static final Comparator<PronunciationView> VARIETY_THEN_RANK =
            Comparator.comparing(PronunciationView::variety).thenComparingInt(PronunciationView::rank);

    public List<DiscoverSenseResponse> toSenses(LexiconEntryResponse entry, String translationLanguage) {
        List<DiscoverSenseResponse> senses = new ArrayList<>();
        for (LexemeView lexeme : entry.lexemes()) {
            String transcription = headwordPronunciations(lexeme)
                    .map(PronunciationView::phonemic)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
            for (SenseView sense : lexeme.senses()) {
                senses.add(new DiscoverSenseResponse(
                        senses.size() + 1,
                        lexeme.headword(),
                        lexeme.pos(),
                        transcription,
                        glosses(sense, translationLanguage),
                        sense.definition(),
                        sense.cefr(),
                        examples(sense, translationLanguage)));
            }
        }
        return senses;
    }

    public List<DiscoverPronunciationResponse> toPronunciations(LexiconEntryResponse entry) {
        Map<List<String>, DiscoverPronunciationResponse> byVarietyAndIpa = new LinkedHashMap<>();
        entry.lexemes().stream()
                .flatMap(this::headwordPronunciations)
                .sorted(VARIETY_THEN_RANK)
                .map(this::toPronunciation)
                .forEach(pronunciation -> byVarietyAndIpa.merge(
                        Arrays.asList(pronunciation.variety(), pronunciation.ipa()),
                        pronunciation,
                        DiscoverLexiconMapper::betterClip));
        return List.copyOf(byVarietyAndIpa.values());
    }

    private static DiscoverPronunciationResponse betterClip(
            DiscoverPronunciationResponse first, DiscoverPronunciationResponse second) {
        return clipRank(second) > clipRank(first) ? second : first;
    }

    private static int clipRank(DiscoverPronunciationResponse pronunciation) {
        if (pronunciation.clipUrl() == null) return 0;
        return "verified".equals(pronunciation.clipStatus()) ? 2 : 1;
    }

    private Stream<PronunciationView> headwordPronunciations(LexemeView lexeme) {
        return lexeme.pronunciations().stream().filter(pronunciation -> pronunciation.formId() == null);
    }

    private DiscoverPronunciationResponse toPronunciation(PronunciationView pronunciation) {
        ClipView best =
                pronunciation.clips().isEmpty() ? null : pronunciation.clips().getFirst();
        return new DiscoverPronunciationResponse(
                pronunciation.variety(),
                pronunciation.phonemic(),
                best == null ? null : best.url(),
                best == null ? null : best.status());
    }

    private List<String> glosses(SenseView sense, String translationLanguage) {
        return sense.glosses().getOrDefault(translationLanguage, List.of()).stream()
                .sorted(EXACT_FIRST)
                .map(GlossView::text)
                .filter(text -> text != null && !text.isBlank())
                .distinct()
                .toList();
    }

    private List<DiscoverExampleResponse> examples(SenseView sense, String translationLanguage) {
        return sense.examples().stream()
                .map(example -> toExample(example, translationLanguage))
                .toList();
    }

    private DiscoverExampleResponse toExample(ExampleView example, String translationLanguage) {
        return new DiscoverExampleResponse(
                example.text(),
                example.translation() == null ? null : example.translation().get(translationLanguage));
    }
}
