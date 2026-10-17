package com.almonium.learning.lexicon.model;

import java.util.List;
import java.util.Map;
import tools.jackson.databind.JsonNode;

/**
 * A dictionary entry as the writers produce it (schema version 2): one headword, the lexemes it stands for, or a
 * reason it stands for none. Read from the published JSON after it has passed the schema, so every field here is
 * present and shaped as the schema says; glosses stay as JSON because a gloss is a string or a {text, exact} object.
 */
public record EntryDocument(String language, String headword, List<LexemeDocument> lexemes, String reject) {

    public List<LexemeDocument> lexemes() {
        return lexemes == null ? List.of() : lexemes;
    }

    public record LexemeDocument(
            String lemma,
            String pos,
            String kind,
            Map<String, Object> grammar,
            List<FormDocument> forms,
            String pattern,
            List<PronunciationDocument> pronunciations,
            List<SenseDocument> senses,
            List<RelationDocument> relations) {

        public Map<String, Object> grammar() {
            return grammar == null ? Map.of() : grammar;
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

    public record FormDocument(String form, Map<String, Object> features, Map<String, String> ipa) {
        public Map<String, Object> features() {
            return features == null ? Map.of() : features;
        }

        public Map<String, String> ipa() {
            return ipa == null ? Map.of() : ipa;
        }
    }

    public record PronunciationDocument(String variety, String ipa) {}

    public record SenseDocument(
            String definition,
            String register,
            String cefr,
            Map<String, JsonNode> glosses,
            String example,
            List<CueDocument> cues,
            Map<String, Object> grammar,
            String usage) {

        public Map<String, JsonNode> glosses() {
            return glosses == null ? Map.of() : glosses;
        }

        public List<CueDocument> cues() {
            return cues == null ? List.of() : cues;
        }
    }

    public record CueDocument(String kind, String text) {}

    public record RelationDocument(String type, String lemma, String pos, String why) {}
}
