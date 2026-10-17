package com.almonium.learning.lexicon.service;

import java.text.Normalizer;
import java.util.Locale;

/** The one spelling a word is looked up by: NFC, lowercase, trimmed. Stored on each form, applied to each query. */
public final class WordNormalizer {
    private WordNormalizer() {}

    public static String normalize(String text) {
        return Normalizer.normalize(text.trim(), Normalizer.Form.NFC).toLowerCase(Locale.ROOT);
    }
}
