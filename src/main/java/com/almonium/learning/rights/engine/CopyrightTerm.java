package com.almonium.learning.rights.engine;

import com.almonium.learning.rights.model.AuthorFacts;
import java.util.OptionalInt;

/**
 * How long one country protects a work. Every term here runs to the end of a calendar year, so the answer is the first
 * year (from 1 January) the work is free.
 */
public interface CopyrightTerm {

    /** The first free year if this author had died in {@code deathYear}. */
    int freeFrom(int deathYear, Integer publicationYear, AuthorFacts author);

    /**
     * A first free year that does not depend on anyone's death, when the term has one (the United States counts
     * older works from publication). Empty for the life-based terms.
     */
    default OptionalInt freeFromPublication(Integer publicationYear) {
        return OptionalInt.empty();
    }

    /** Life plus {@code years}: the most common term, and the one Ukraine and Germany use. */
    static CopyrightTerm lifePlus(int years) {
        return (deathYear, publicationYear, author) -> deathYear + years + 1;
    }

    /**
     * A term that was lengthened without reviving anything: authors who died in {@code lastDeathUnderOldTerm} or
     * before keep the old, shorter term (Canada, New Zealand, Japan, South Korea).
     */
    static CopyrightTerm extendedWithoutRevival(int oldYears, int newYears, int lastDeathUnderOldTerm) {
        return (deathYear, publicationYear, author) ->
                deathYear + (deathYear <= lastDeathUnderOldTerm ? oldYears : newYears) + 1;
    }
}
