package com.almonium.analyzer.analyzer.mapper;

import com.almonium.analyzer.analyzer.dto.DiscoverExampleResponse;
import com.almonium.analyzer.analyzer.dto.DiscoverHomophoneResponse;
import com.almonium.analyzer.analyzer.dto.DiscoverPronunciationResponse;
import com.almonium.analyzer.analyzer.dto.DiscoverSenseResponse;
import com.almonium.analyzer.analyzer.dto.IpaSegmentResponse;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.ClipView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.ExampleView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.GlossView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.HomophoneView;
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
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

/**
 * Flattens a lexicon entry onto the Discover sheet, which knows one list of senses and one of pronunciations and
 * nothing of lexemes. Senses are numbered across lexemes in order; pronunciations are the headword-level ones that
 * have a transcription (the sheet has no place yet for a reading in kana or with stress marks), by variety then rank, each carrying its best clip (a verified one before an unverified one, which is the order the
 * lexicon already lists them in). A word with several lexemes (clear as adjective, verb and adverb) records the same
 * pronunciation on each, so one variety saying the same IPA is shown once, with the best clip any lexeme has, and
 * with the numbers of every sense said that way: live gives /laɪv/ the adjective's senses and /lɪv/ the verb's, bow
 * gives /boʊ/ only the weapon's, while niche's two readings both carry all of its senses. Each carries its
 * transcription cut where it parts from the others ({@link TranscriptionDiff}), computed per response: from every
 * other variety's while the readings do not follow the meaning (design 5a), and otherwise only from its own variety's
 * readings for other senses (design 7a): the card then sets readings in columns by meaning, and the tint marks what
 * tells the columns apart, never two alternatives taken by the same senses.
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
                        sense.key(),
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

    /**
     * The words said like the sheet's word, one row a spelling, gathering the varieties and the senses (numbered as
     * {@link #toSenses} numbers them) of every lexeme that sounds like it.
     */
    public List<DiscoverHomophoneResponse> toHomophones(LexiconEntryResponse entry) {
        Map<String, Set<String>> varieties = new LinkedHashMap<>();
        Map<String, Set<Integer>> senses = new LinkedHashMap<>();
        int senseCount = 0;
        for (LexemeView lexeme : entry.lexemes()) {
            int first = senseCount + 1;
            senseCount += lexeme.senses().size();
            for (HomophoneView homophone : lexeme.homophones()) {
                varieties
                        .computeIfAbsent(homophone.lemma(), word -> new TreeSet<>())
                        .addAll(homophone.varieties());
                Set<Integer> numbers = senses.computeIfAbsent(homophone.lemma(), word -> new TreeSet<>());
                IntStream.rangeClosed(first, senseCount).forEach(numbers::add);
            }
        }
        return varieties.entrySet().stream()
                .map(word -> new DiscoverHomophoneResponse(
                        word.getKey(), List.copyOf(word.getValue()), List.copyOf(senses.get(word.getKey()))))
                .toList();
    }

    public List<DiscoverPronunciationResponse> toPronunciations(LexiconEntryResponse entry) {
        record Said(PronunciationView pronunciation, List<Integer> senses) {}
        List<Said> said = new ArrayList<>();
        int senseCount = 0;
        for (LexemeView lexeme : entry.lexemes()) {
            List<Integer> senses = IntStream.rangeClosed(
                            senseCount + 1, senseCount + lexeme.senses().size())
                    .boxed()
                    .toList();
            senseCount += senses.size();
            headwordPronunciations(lexeme).forEach(pronunciation -> said.add(new Said(pronunciation, senses)));
        }
        Map<List<String>, DiscoverPronunciationResponse> byVarietyAndIpa = new LinkedHashMap<>();
        said.stream()
                .sorted(Comparator.comparing(Said::pronunciation, VARIETY_THEN_RANK))
                .map(each -> toPronunciation(each.pronunciation(), each.senses()))
                .forEach(pronunciation -> byVarietyAndIpa.merge(
                        Arrays.asList(pronunciation.variety(), pronunciation.ipa()),
                        pronunciation,
                        DiscoverLexiconMapper::merged));
        List<DiscoverPronunciationResponse> pronunciations = List.copyOf(byVarietyAndIpa.values());
        List<List<IpaSegmentResponse>> segments = TranscriptionDiff.segments(
                pronunciations.stream().map(DiscoverPronunciationResponse::ipa).toList(),
                entry.language(),
                followsMeaning(pronunciations)
                        ? (first, second) -> apart(pronunciations.get(first), pronunciations.get(second))
                        : (first, second) -> true);
        List<DiscoverPronunciationResponse> cut = new ArrayList<>();
        for (int index = 0; index < pronunciations.size(); index++) {
            cut.add(pronunciations.get(index).withSegments(segments.get(index)));
        }
        return List.copyOf(cut);
    }

    /** Whether some reading is said for fewer than all the senses that have one, so the card groups them by meaning. */
    private static boolean followsMeaning(List<DiscoverPronunciationResponse> pronunciations) {
        long said = pronunciations.stream()
                .flatMap(pronunciation -> pronunciation.senses().stream())
                .distinct()
                .count();
        return pronunciations.stream()
                .anyMatch(pronunciation -> pronunciation.senses().size() < said);
    }

    /** Two readings of one variety said for different senses, so at least one column holds one and not the other. */
    private static boolean apart(DiscoverPronunciationResponse first, DiscoverPronunciationResponse second) {
        return first.variety().equals(second.variety()) && !first.senses().equals(second.senses());
    }

    /** One variety's IPA recorded by two lexemes: the better clip, said for both lexemes' senses. */
    private static DiscoverPronunciationResponse merged(
            DiscoverPronunciationResponse first, DiscoverPronunciationResponse second) {
        DiscoverPronunciationResponse clipped = betterClip(first, second);
        List<Integer> senses = Stream.concat(first.senses().stream(), second.senses().stream())
                .distinct()
                .sorted()
                .toList();
        return clipped.withSenses(senses);
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
        return lexeme.pronunciations().stream()
                .filter(pronunciation -> pronunciation.formId() == null && pronunciation.phonemic() != null);
    }

    private DiscoverPronunciationResponse toPronunciation(PronunciationView pronunciation, List<Integer> senses) {
        ClipView best =
                pronunciation.clips().isEmpty() ? null : pronunciation.clips().getFirst();
        return new DiscoverPronunciationResponse(
                pronunciation.variety(),
                pronunciation.phonemic(),
                best == null ? null : best.url(),
                best == null ? null : best.status(),
                null,
                senses);
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
