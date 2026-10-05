package com.almonium.learning.rights.model;

/**
 * One author (or human translator) as far as copyright terms care. Years only: every term in the table runs to the
 * end of a calendar year. Unknown years are null.
 *
 * @param diedForFrance the author was declared "mort pour la France": France adds 30 years
 * @param rehabilitatedYear the year a repressed author was posthumously rehabilitated: Ukraine and Russia count the
 *     term from it instead of from the death
 * @param repressed the author died in Soviet repression, so may have been rehabilitated after death even when no
 *     year is recorded
 */
public record AuthorFacts(
        String name,
        Integer birthYear,
        Integer deathYear,
        boolean diedForFrance,
        Integer rehabilitatedYear,
        boolean repressed) {

    public AuthorFacts(
            String name, Integer birthYear, Integer deathYear, boolean diedForFrance, Integer rehabilitatedYear) {
        this(name, birthYear, deathYear, diedForFrance, rehabilitatedYear, false);
    }

    public static AuthorFacts of(String name, Integer birthYear, Integer deathYear) {
        return new AuthorFacts(name, birthYear, deathYear, false, null);
    }
}
