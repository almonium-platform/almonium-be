package com.almonium.learning.rights.engine;

import static com.almonium.learning.rights.engine.CopyrightTerm.extendedWithoutRevival;
import static com.almonium.learning.rights.engine.CopyrightTerm.lifePlus;

import com.almonium.learning.rights.model.AuthorFacts;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;

/**
 * The copyright term of every country, as of 2026. Most of the world, including Ukraine, Germany and the rest of the
 * EU, is life plus 70, which is also the default for any country not listed. Where a country's term is not known for
 * certain the table errs long: a term guessed too long only hides a book, a term guessed too short publishes one.
 * Overrides in /ops handle what a table cannot (a takedown, a court ruling, a single contested work).
 */
public final class CountryTerms {
    public static final CopyrightTerm LIFE_PLUS_70 = lifePlus(70);

    /**
     * United States: works published before 1978 are protected 95 years from publication (renewal lapses ignored,
     * which errs long); later or unpublished works life plus 70.
     */
    public static final CopyrightTerm UNITED_STATES = new CopyrightTerm() {
        @Override
        public int freeFrom(int deathYear, Integer publicationYear, AuthorFacts a) {
            if (publicationYear != null && publicationYear <= 1977) {
                return publicationYear + 96;
            }
            return deathYear + 71;
        }

        @Override
        public OptionalInt freeFromPublication(Integer publicationYear) {
            return publicationYear != null && publicationYear <= 1977
                    ? OptionalInt.of(publicationYear + 96)
                    : OptionalInt.empty();
        }
    };

    /**
     * France: life plus 70. For an author declared "mort pour la France" the old term still runs: life plus 50, plus
     * 30 years, plus the war extensions for works published by the end of 1947 (8 years 120 days) or of 1920 (6 years
     * 152 days more), each rounded up to the next year end; whichever is longer.
     */
    public static final CopyrightTerm FRANCE = (deathYear, publicationYear, author) -> {
        int standard = deathYear + 71;
        if (author == null || !author.diedForFrance()) {
            return standard;
        }
        int wartime =
                publicationYear == null ? 9 + 7 : (publicationYear <= 1947 ? 9 : 0) + (publicationYear <= 1920 ? 7 : 0);
        return Math.max(standard, deathYear + 50 + 30 + wartime + 1);
    };

    /** Spain: authors who died before 7 December 1987 keep the old life plus 80. */
    public static final CopyrightTerm SPAIN =
            (deathYear, publicationYear, author) -> deathYear + (deathYear <= 1987 ? 80 : 70) + 1;

    /**
     * Ukraine: life plus 70, counted from rehabilitation for a repressed author rehabilitated after death.
     */
    public static final CopyrightTerm UKRAINE =
            (deathYear, publicationYear, author) -> Math.max(deathYear, rehabilitation(author, deathYear)) + 71;

    /**
     * Russia: life plus 70; four more years for an author who worked or fought during the Great Patriotic War (taken
     * as anyone who died in 1941 or later), counted from rehabilitation for a rehabilitated author. A work whose old
     * term (life plus 50, or 54) had run out by 1 January 1993 was not brought back.
     */
    public static final CopyrightTerm RUSSIA = (deathYear, publicationYear, author) -> {
        int from = Math.max(deathYear, rehabilitation(author, deathYear));
        int wartime = deathYear >= 1941 ? 4 : 0;
        int oldTermEnds = from + 50 + wartime + 1;
        return oldTermEnds <= 1993 ? oldTermEnds : from + 70 + wartime + 1;
    };

    private static final Map<String, CopyrightTerm> TERMS = new HashMap<>();

    static {
        put(
                lifePlus(50),
                "BY",
                "CN",
                "EG",
                "HK",
                "IR",
                "KE",
                "MO",
                "MY",
                "NP",
                "PH",
                "PK",
                "SA",
                "TH",
                "TW",
                "VN",
                "ZA");
        put(lifePlus(60), "BD", "IN", "VE");
        put(lifePlus(75), "GT", "HN", "VC", "WS");
        put(lifePlus(80), "CO", "GQ");
        put(lifePlus(95), "JM");
        put(lifePlus(99), "CI");
        put(lifePlus(100), "MX");
        put(extendedWithoutRevival(50, 70, 1971), "CA", "NZ");
        // Japan moved to 70 at the end of 2018; works by Allied nationals also carry a wartime extension of about ten
        // years, so the old-term cutoff is pulled back a decade rather than tracking nationality.
        put(extendedWithoutRevival(50, 70, 1957), "JP");
        put(extendedWithoutRevival(50, 70, 1962), "KR");
        put(UNITED_STATES, "US", "PR", "GU", "VI", "AS", "MP", "UM");
        put(FRANCE, "FR", "GF", "GP", "MQ", "RE", "YT", "PM", "BL", "MF", "NC", "PF", "WF");
        put(SPAIN, "ES");
        put(UKRAINE, "UA");
        put(RUSSIA, "RU");
    }

    /** The term for an ISO 3166 alpha-2 country code; life plus 70 when the country is not listed. */
    public static CopyrightTerm of(String country) {
        return TERMS.getOrDefault(country.toUpperCase(Locale.ROOT), LIFE_PLUS_70);
    }

    /** Every ISO 3166 alpha-2 code the JDK knows, which is the set a book can be blocked in. */
    public static Set<String> countries() {
        return Locale.getISOCountries(Locale.IsoCountryCode.PART1_ALPHA2);
    }

    private CountryTerms() {}

    private static int rehabilitation(AuthorFacts author, int deathYear) {
        return author == null || author.rehabilitatedYear() == null ? deathYear : author.rehabilitatedYear();
    }

    private static void put(CopyrightTerm term, String... countries) {
        for (String country : countries) {
            TERMS.put(country, term);
        }
    }
}
