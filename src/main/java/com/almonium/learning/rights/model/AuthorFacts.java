package com.almonium.learning.rights.model;

/**
 * One author (or human translator) as far as copyright terms care. Years only: every term in the table runs to the
 * end of a calendar year. Unknown years are null.
 *
 * @param diedForFrance the author was declared "mort pour la France": France adds 30 years
 * @param rehabilitatedYear the year a repressed author was posthumously rehabilitated: Ukraine and Russia count the
 *     term from it instead of from the death
 */
public record AuthorFacts(
        String name, Integer birthYear, Integer deathYear, boolean diedForFrance, Integer rehabilitatedYear) {

    public static AuthorFacts of(String name, Integer birthYear, Integer deathYear) {
        return new AuthorFacts(name, birthYear, deathYear, false, null);
    }
}
