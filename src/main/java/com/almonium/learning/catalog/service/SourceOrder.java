package com.almonium.learning.catalog.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Which library's text to prefer, per language of the text. Decided by the owner on 2026-10-24: a language's list
 * names the libraries that carry it, best first, and a library that does not carry the language is simply not on
 * its list. Configured as {@code app.catalog.source-order}, e.g. {@code en=STANDARD_EBOOKS,GUTENBERG;*=GUTENBERG}.
 * A kind not on the language's list ranks after every kind that is, in the order of the {@code *} list, and a
 * kind on neither comes last.
 */
@Component
public class SourceOrder {
    private final Map<String, List<String>> byLanguage = new HashMap<>();

    public SourceOrder(@Value("${app.catalog.source-order:*=GUTENBERG,WIKISOURCE}") String configured) {
        for (String entry : configured.split(";")) {
            String[] pair = entry.trim().split("=", 2);
            if (pair.length == 2 && !pair[0].isBlank()) {
                byLanguage.put(
                        pair[0].trim().toLowerCase(Locale.ROOT),
                        Arrays.stream(pair[1].split(","))
                                .map(String::trim)
                                .filter(kind -> !kind.isEmpty())
                                .map(kind -> kind.toUpperCase(Locale.ROOT))
                                .toList());
            }
        }
    }

    /** The rank of a library for a text in {@code language}: 0 is the most preferred. */
    public int rank(String language, String kind) {
        List<String> preferred =
                byLanguage.getOrDefault(language == null ? "" : language.toLowerCase(Locale.ROOT), List.of());
        List<String> general = byLanguage.getOrDefault("*", List.of());
        int index = preferred.indexOf(kind == null ? "" : kind.toUpperCase(Locale.ROOT));
        if (index >= 0) {
            return index;
        }
        int fallback = general.indexOf(kind == null ? "" : kind.toUpperCase(Locale.ROOT));
        return preferred.size() + (fallback >= 0 ? fallback : general.size());
    }
}
