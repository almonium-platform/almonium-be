package com.almonium.learning.lexicon.dto.response;

/**
 * What became of one published entry: accepted with the number of lexemes now standing for the headword (zero for a
 * rejected headword), or rejected with the reason, which is the schema's message when the body did not pass it.
 */
public record EntryPublicationResult(String language, String headword, boolean accepted, int lexemes, String message) {

    public static EntryPublicationResult accepted(String language, String headword, int lexemes) {
        return new EntryPublicationResult(language, headword, true, lexemes, null);
    }

    public static EntryPublicationResult rejected(String language, String headword, String message) {
        return new EntryPublicationResult(language, headword, false, 0, message);
    }
}
