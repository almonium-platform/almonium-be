package com.almonium.learning.rights.engine;

import com.almonium.learning.rights.model.AuthorFacts;
import com.almonium.learning.rights.model.CountryVerdict;
import com.almonium.learning.rights.model.RightsFacts;
import com.almonium.learning.rights.model.RightsStatus;
import com.almonium.learning.rights.model.YearRange;
import java.util.Collection;
import java.util.List;
import java.util.OptionalInt;
import org.springframework.stereotype.Component;

/**
 * Works out whether a work is free in a country in a given year, from its publication year and its authors' lives.
 * Missing facts widen the answer instead of guessing: an author with no recorded death is assumed to have died some
 * time between the publication (or their birth) and 110 years after their birth, and the work is called free only if
 * it is free for every death in that window. A work is free when its last author's term has run out.
 *
 * <p>This is a careful estimate for deciding what to prepare and show, not a legal opinion; /ops overrides win.
 */
@Component
public class CopyrightEngine {
    /** Nobody is assumed to live longer than this; it bounds an unknown death year. */
    static final int LONGEST_LIFE = 110;

    public CountryVerdict verdict(RightsFacts facts, String country, int year) {
        YearRange freeFrom = freeFrom(facts, CountryTerms.of(country));
        return new CountryVerdict(country, freeFrom.statusIn(year), freeFrom);
    }

    /**
     * Free in all of the countries: the status a work needs to be prepared (Ukraine, where editions are made, and
     * Germany, where they are hosted). Protected if any one country protects it, unknown otherwise.
     */
    public RightsStatus statusInAll(RightsFacts facts, Collection<String> countries, int year) {
        boolean unknown = false;
        for (String country : countries) {
            RightsStatus status = verdict(facts, country, year).status();
            if (status == RightsStatus.PROTECTED) {
                return RightsStatus.PROTECTED;
            }
            unknown |= status == RightsStatus.UNKNOWN;
        }
        return unknown ? RightsStatus.UNKNOWN : RightsStatus.FREE;
    }

    /** The years a work becomes free in under one country's term, as tight as the facts allow. */
    YearRange freeFrom(RightsFacts facts, CopyrightTerm term) {
        OptionalInt fixed = term.freeFromPublication(facts.publicationYear());
        if (fixed.isPresent()) {
            return YearRange.exactly(fixed.getAsInt());
        }
        List<AuthorFacts> authors =
                facts.authors().isEmpty() ? List.of(AuthorFacts.of(null, null, null)) : facts.authors();
        Integer earliest = null;
        Integer latest = Integer.MIN_VALUE;
        for (AuthorFacts author : authors) {
            YearRange one = freeFrom(author, facts.publicationYear(), term);
            if (one.earliest() != null) {
                earliest = earliest == null ? one.earliest() : Math.max(earliest, one.earliest());
            }
            latest = latest == null || one.latest() == null ? null : Math.max(latest, one.latest());
        }
        return new YearRange(earliest, latest);
    }

    private static YearRange freeFrom(AuthorFacts author, Integer publicationYear, CopyrightTerm term) {
        YearRange deaths = deathYears(author, publicationYear);
        if (deaths.earliest() == null || deaths.latest() == null) {
            return YearRange.open();
        }
        int earliest = Integer.MAX_VALUE;
        int latest = Integer.MIN_VALUE;
        // Terms are not always monotonic in the death year (Spain's 1987 switch), so try every year in the window.
        for (int death = deaths.earliest(); death <= deaths.latest(); death++) {
            int free = term.freeFrom(death, publicationYear, author);
            earliest = Math.min(earliest, free);
            latest = Math.max(latest, free);
        }
        return new YearRange(earliest, latest);
    }

    /** The window the author died in: exact when recorded, otherwise from what birth and publication allow. */
    static YearRange deathYears(AuthorFacts author, Integer publicationYear) {
        if (author.deathYear() != null) {
            return YearRange.exactly(author.deathYear());
        }
        Integer bornBy = author.birthYear() != null ? author.birthYear() : publicationYear;
        if (bornBy == null) {
            return YearRange.open();
        }
        // Alive at publication unless the death is recorded: posthumous works carry their author's death date.
        int earliest = publicationYear != null ? Math.max(bornBy, publicationYear) : bornBy;
        return new YearRange(earliest, bornBy + LONGEST_LIFE);
    }
}
