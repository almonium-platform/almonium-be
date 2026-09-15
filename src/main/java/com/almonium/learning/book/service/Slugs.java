package com.almonium.learning.book.service;

import java.text.Normalizer;
import java.util.Locale;

/** The processor's Django {@code slugify}, so a title slugs to the same work slug on both sides. */
final class Slugs {
    private Slugs() {}

    static String slugify(String value) {
        if (value == null) {
            return "";
        }
        String ascii = Normalizer.normalize(value, Normalizer.Form.NFKD).replaceAll("[^\\p{ASCII}]", "");
        String cleaned =
                ascii.toLowerCase(Locale.ROOT).replaceAll("[^\\w\\s-]", "").trim();
        return cleaned.replaceAll("[-\\s]+", "-").replaceAll("^[-_]+|[-_]+$", "");
    }
}
