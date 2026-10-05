package com.almonium.learning.catalog.service;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Folds a title or a name to what search compares: lower case, accents and apostrophes gone, punctuation as spaces.
 * "Bahnwärter Thiel" and "bahnwarter thiel" fold to the same string, and so do "Ёлка" and "елка". The same folding is
 * applied to the stored titles and to the query, so the trigram index only ever sees folded text.
 */
public final class TitleFolding {
    private static final Pattern MARKS = Pattern.compile("\\p{M}+");
    private static final Pattern APOSTROPHES = Pattern.compile("['’ʼ`´]");
    private static final Pattern NOT_WORD = Pattern.compile("[^\\p{L}\\p{N}]+");

    private TitleFolding() {}

    public static String fold(String text) {
        if (text == null) {
            return "";
        }
        String lower = text.toLowerCase(Locale.ROOT)
                .replace("ß", "ss")
                .replace("ё", "е")
                .replace("ї", "і");
        String unmarked =
                MARKS.matcher(Normalizer.normalize(lower, Normalizer.Form.NFD)).replaceAll("");
        String joined = APOSTROPHES.matcher(unmarked).replaceAll("");
        return NOT_WORD.matcher(joined).replaceAll(" ").trim();
    }

    /** The main title without a subtitle: "Frankenstein; or, the modern prometheus" is "Frankenstein". */
    public static String mainTitle(String title) {
        if (title == null) {
            return "";
        }
        String firstLine = title.lines().findFirst().orElse("").trim();
        int cut = firstLine.length();
        for (String separator : new String[] {";", ": ", " — ", " - "}) {
            int at = firstLine.indexOf(separator);
            if (at > 0) {
                cut = Math.min(cut, at);
            }
        }
        return firstLine.substring(0, cut).trim();
    }
}
