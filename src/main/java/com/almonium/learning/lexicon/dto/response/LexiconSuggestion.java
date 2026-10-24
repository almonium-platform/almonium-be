package com.almonium.learning.lexicon.dto.response;

/**
 * One row under a field being typed into: a headword beginning with what was typed (or with an inflected form that
 * does), its part of speech, its grammatical gender where the language marks one (Fem, Masc, Neut), the first gloss
 * of its first sense in the reader's language, and how often its spelling occurs on the Zipf scale. {@code gloss}
 * is null when the entry has none in that language, {@code zipf} when the entry was published without one.
 */
public record LexiconSuggestion(String headword, String pos, String gender, String gloss, Double zipf) {}
