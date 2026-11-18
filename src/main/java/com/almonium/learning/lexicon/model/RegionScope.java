package com.almonium.learning.lexicon.model;

/**
 * Where a lexeme, form or sense is used when not everywhere its language is: the language with a country (es-EC,
 * en-GB) or a UN M.49 area (es-419), and whether the use is {@code only} there or {@code mainly} there.
 */
public record RegionScope(String variety, String extent) {
    public static final String ONLY = "only";
}
