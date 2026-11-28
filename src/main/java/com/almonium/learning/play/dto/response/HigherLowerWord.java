package com.almonium.learning.play.dto.response;

/**
 * One word a Higher or Lower run may place: its lemma, its one-line meaning in the reader's language, how often it
 * turns up in print (uses per million words, from the lexicon's Zipf score) and its rank among the language's words
 * by that figure.
 */
public record HigherLowerWord(String headword, String gloss, double perMillion, int rank) {}
