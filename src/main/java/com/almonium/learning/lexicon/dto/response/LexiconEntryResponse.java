package com.almonium.learning.lexicon.dto.response;

import com.almonium.learning.lexicon.model.RegionScope;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * What a reader's word resolves to in one language: every lexeme it names, by headword or lemma, or by one of its
 * forms ({@code matchedForm} then says which). Every surface reads this one shape; a field the lexicon has nothing
 * for is absent, and {@code capabilities} says so up front. Provenance travels with each lexeme.
 */
public record LexiconEntryResponse(String language, String text, List<LexemeView> lexemes) {

    public record LexemeView(
            UUID id,
            String headword,
            String lemma,
            String pos,
            int homographNo,
            String kind,
            Map<String, Object> grammar,
            List<Map<String, Object>> spellings,
            List<Map<String, Object>> components,
            Boolean separable,
            Double zipf,
            String status,
            FormView matchedForm,
            List<FormView> forms,
            List<PronunciationView> pronunciations,
            List<SenseView> senses,
            List<RelationView> relations,
            List<HomophoneView> homophones,
            List<PartView> parts,
            Capabilities capabilities,
            Provenance provenance) {}

    /**
     * Another word said exactly like this lexeme, found by comparing transcriptions, not written by the entry's
     * writer; {@code varieties} are those that say the two alike (an American merger makes more pairs).
     */
    public record HomophoneView(String lemma, String pos, List<String> varieties) {}

    /**
     * {@code varieties} and {@code senseKeys} say which varieties use the form and which senses it belongs to, null
     * for all of them; {@code reading} is how it is read in {@code notation}, {@code note} where it is used.
     */
    public record FormView(
            UUID id,
            String form,
            Map<String, Object> features,
            List<String> varieties,
            List<UUID> senseKeys,
            String reading,
            String notation,
            String note) {}

    /**
     * {@code phonemic} is the IPA transcription and {@code reading} the word as read in {@code notation} (kana, the
     * spelling with its stress marked); an entry gives one or both. {@code spelling} names the spelling the reading
     * belongs to when the lemma's spellings are said differently; {@code accent} is the Japanese pitch accent.
     */
    public record PronunciationView(
            UUID id,
            UUID formId,
            String variety,
            int rank,
            String phonemic,
            String reading,
            String notation,
            Integer accent,
            String spelling,
            String note,
            String status,
            List<ClipView> clips) {}

    /**
     * One synthesised rendering of a pronunciation, verified ones first. {@code url} is the path under the API base
     * ({@code /public/lexicon/clips/{id}}); a client prepends its own base.
     */
    public record ClipView(
            UUID id,
            String engine,
            String voice,
            BigDecimal pace,
            String format,
            int durationMs,
            String status,
            String url) {}

    /**
     * {@code key} is the sense's identity across rewrites of the entry, what a card saved from it stores.
     * {@code labels} says what kind of word this is here, each axis by itself (formality, attitude, time, style,
     * normativity, domain, figurative, rare, politeness). {@code frames} are how the word is built into a sentence
     * and {@code patterns} those frames read into parts; {@code collocations} the words it is commonly said with,
     * the less guessable first. Glosses and cues are empty until their passes have run.
     */
    public record SenseView(
            UUID id,
            UUID key,
            int position,
            String definition,
            String cefr,
            String cefrSource,
            Map<String, Object> labels,
            Map<String, Object> grammar,
            List<String> frames,
            List<Map<String, Object>> patterns,
            List<String> collocations,
            List<Map<String, Object>> antonyms,
            List<String> spellings,
            String standardAlternative,
            List<RegionScope> regions,
            Map<String, List<GlossView>> glosses,
            List<ExampleView> examples,
            List<CueView> cues) {}

    public record GlossView(String text, Boolean exact, String collidesWith) {}

    public record ExampleView(String text, String origin, Map<String, String> translation) {}

    public record CueView(String kind, String text, String difficulty) {}

    /** {@code senses} are the senses of this lexeme, counted from 1, the relation holds for; null for all. */
    public record RelationView(
            String type, String lemma, String pos, UUID lexemeId, String evidence, List<Integer> senses) {}

    public record PartView(String text, String gloss, String role) {}

    /** What this lexeme can offer a client, so it never guesses at an absent field. */
    public record Capabilities(
            boolean forms, boolean senseCefr, boolean ipa, boolean ipaAudio, boolean parts, boolean relations) {}

    public record Provenance(
            String writer,
            String model,
            String promptVersion,
            String registerVersion,
            String schemaVersion,
            int entryVersion,
            Instant builtAt) {}
}
