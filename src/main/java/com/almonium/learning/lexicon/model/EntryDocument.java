package com.almonium.learning.lexicon.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import tools.jackson.databind.JsonNode;

/**
 * A dictionary entry as it is published (schema version 4): the entry a writer returned, cleaned by the ingest step,
 * which gives every lexeme and sense its id, reads the frames into parts and lists the spellings the entry answers
 * for. One headword, the lexemes it stands for, or a reason it stands for none. Read from the published JSON after it
 * has passed the schema, so every field here is present and shaped as the schema says. Glosses and review cues are
 * not part of an entry: later passes write them.
 */
public record EntryDocument(
        String language, String headword, List<LexemeDocument> lexemes, String reject, List<String> aliases) {

    public List<LexemeDocument> lexemes() {
        return lexemes == null ? List.of() : lexemes;
    }

    public List<String> aliases() {
        return aliases == null ? List.of() : aliases;
    }

    /**
     * {@code homograph} tells apart two lexemes of one lemma and part of speech; ingest keeps it across rewrites.
     * {@code retired} are the senses an earlier version of the lexeme had and this one has not.
     */
    public record LexemeDocument(
            UUID id,
            int homograph,
            String lemma,
            String pos,
            String kind,
            List<SpellingDocument> spellings,
            Map<String, Object> grammar,
            List<FormDocument> forms,
            List<PronunciationDocument> pronunciations,
            List<ComponentDocument> components,
            Boolean separable,
            List<SenseDocument> senses,
            List<RelationDocument> relations,
            List<RetiredSense> retired) {

        public Map<String, Object> grammar() {
            return grammar == null ? Map.of() : grammar;
        }

        public List<SpellingDocument> spellings() {
            return spellings == null ? List.of() : spellings;
        }

        public List<FormDocument> forms() {
            return forms == null ? List.of() : forms;
        }

        public List<PronunciationDocument> pronunciations() {
            return pronunciations == null ? List.of() : pronunciations;
        }

        public List<SenseDocument> senses() {
            return senses == null ? List.of() : senses;
        }

        public List<RelationDocument> relations() {
            return relations == null ? List.of() : relations;
        }
    }

    /** One way the lemma is written, where and in which script; {@code preReform} for a spelling a reform retired. */
    public record SpellingDocument(
            String text,
            List<String> varieties,
            @JsonProperty("pre_reform") Boolean preReform,
            String script) {}

    /** A word of a multiword lexeme: as written, its lemma, and whether it inflects (head, agrees, fixed). */
    public record ComponentDocument(String text, String lemma, String inflects) {}

    public record FormDocument(
            String form,
            Map<String, Object> features,
            Map<String, String> ipa,
            List<String> varieties,
            @JsonProperty("sense_ids") List<UUID> senseIds,
            String reading,
            String notation,
            String note) {
        public Map<String, Object> features() {
            return features == null ? Map.of() : features;
        }

        public Map<String, String> ipa() {
            return ipa == null ? Map.of() : ipa;
        }
    }

    /** A transcription, a reading in {@code notation} (kana, the spelling with its stress marked), or both. */
    public record PronunciationDocument(
            String variety,
            String ipa,
            String reading,
            String notation,
            String note,
            String spelling,
            Integer accent) {}

    public record SenseDocument(
            UUID id,
            String definition,
            String example,
            String cefr,
            List<String> frames,
            List<Map<String, Object>> patterns,
            List<String> collocations,
            Map<String, Object> labels,
            List<JsonNode> regions,
            @JsonProperty("standard_alternative") String standardAlternative,
            Map<String, Object> grammar,
            List<Map<String, Object>> antonyms,
            List<String> from,
            List<String> spellings) {

        /** A region written as a bare variety (en-GB) means only there. */
        public List<RegionScope> scopes() {
            if (regions == null || regions.isEmpty()) {
                return null;
            }
            return regions.stream()
                    .map(region -> region.isString()
                            ? new RegionScope(region.stringValue(), RegionScope.ONLY)
                            : new RegionScope(
                                    region.path("variety").asString(),
                                    region.path("extent").asString()))
                    .toList();
        }
    }

    public record RelationDocument(String type, String lemma, String pos, String why, List<Integer> senses) {}

    public record RetiredSense(UUID id, String definition) {}
}
