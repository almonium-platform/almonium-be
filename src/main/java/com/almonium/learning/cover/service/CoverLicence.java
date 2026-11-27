package com.almonium.learning.cover.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Whether a Commons file is free to take, read from the hidden categories its licence templates put it in. A file is
 * taken when Commons marks its author as dead long enough ({@code PD-old-70-expired}, {@code PD-Art (PD-old-100)},
 * or the plain {@code PD Old}, which on Commons means seventy years) or the file as CC0. Anything else is left:
 * "public domain in the United States" alone says nothing about Europe, an assumed date is not a date, and a free
 * licence that asks for attribution is a duty a cover has no room to meet.
 */
public final class CoverLicence {
    private static final Pattern AUTHOR_DEAD = Pattern.compile("PD-old-(\\d{2,3})");
    /** {@code {{PD-old}}} without a number: the author died more than seventy years ago. */
    private static final Pattern AUTHOR_DEAD_SEVENTY = Pattern.compile("(?i)^PD[- ]old$");

    private static final int SEVENTY = 70;
    private static final Pattern CC_ZERO = Pattern.compile("(?i)\\bCC[- ]?(zero|0)\\b");

    private CoverLicence() {}

    /** The category the file is accepted on, or empty when nothing on its page clears it. */
    public static Optional<String> basis(List<String> hiddenCategories, int minYearsSinceDeath) {
        for (String category : hiddenCategories) {
            Matcher matcher = AUTHOR_DEAD.matcher(category);
            if (matcher.find() && Integer.parseInt(matcher.group(1)) >= minYearsSinceDeath) {
                return Optional.of(category);
            }
        }
        return hiddenCategories.stream()
                .filter(category -> CC_ZERO.matcher(category).find()
                        || (minYearsSinceDeath <= SEVENTY
                                && AUTHOR_DEAD_SEVENTY.matcher(category).matches()))
                .findFirst();
    }
}
