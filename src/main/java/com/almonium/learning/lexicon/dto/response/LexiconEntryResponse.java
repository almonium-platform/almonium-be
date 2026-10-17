package com.almonium.learning.lexicon.dto.response;

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
            String pattern,
            Double zipf,
            String status,
            FormView matchedForm,
            List<FormView> forms,
            List<PronunciationView> pronunciations,
            List<SenseView> senses,
            List<RelationView> relations,
            List<PartView> parts,
            Capabilities capabilities,
            Provenance provenance) {}

    public record FormView(UUID id, String form, Map<String, Object> features) {}

    public record PronunciationView(UUID id, UUID formId, String variety, int rank, String phonemic, String status) {}

    public record SenseView(
            UUID id,
            int position,
            String definition,
            String cefr,
            String cefrSource,
            String register,
            Map<String, Object> grammar,
            String usage,
            List<String> regions,
            Map<String, List<GlossView>> glosses,
            List<ExampleView> examples,
            List<CueView> cues) {}

    public record GlossView(String text, Boolean exact, String collidesWith) {}

    public record ExampleView(String text, String origin, Map<String, String> translation) {}

    public record CueView(String kind, String text, String difficulty) {}

    public record RelationView(String type, String lemma, String pos, UUID lexemeId, String evidence) {}

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
