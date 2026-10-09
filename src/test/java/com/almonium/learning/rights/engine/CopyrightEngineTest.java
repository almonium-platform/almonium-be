package com.almonium.learning.rights.engine;

import static com.almonium.learning.rights.model.RightsStatus.FREE;
import static com.almonium.learning.rights.model.RightsStatus.PROTECTED;
import static com.almonium.learning.rights.model.RightsStatus.UNKNOWN;
import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.learning.rights.model.AuthorFacts;
import com.almonium.learning.rights.model.RightsFacts;
import com.almonium.learning.rights.model.RightsStatus;
import com.almonium.learning.rights.model.YearRange;
import java.util.List;
import org.junit.jupiter.api.Test;

class CopyrightEngineTest {
    static final int YEAR = 2026;
    static final List<String> PRODUCTION = List.of("UA", "DE");

    final CopyrightEngine engine = new CopyrightEngine();

    static RightsFacts work(Integer published, AuthorFacts... authors) {
        return new RightsFacts(published, List.of(authors));
    }

    RightsStatus status(RightsFacts facts, String country) {
        return engine.verdict(facts, country, YEAR).status();
    }

    @Test
    void theLittlePrinceIsFreeInGermanyAndUkraineButNotInFranceOrTheUnitedStates() {
        RightsFacts littlePrince = work(1943, new AuthorFacts("Antoine de Saint-Exupéry", 1900, 1944, true, null));

        assertThat(status(littlePrince, "DE")).isEqualTo(FREE);
        assertThat(status(littlePrince, "UA")).isEqualTo(FREE);
        assertThat(status(littlePrince, "PL")).isEqualTo(FREE);
        assertThat(engine.verdict(littlePrince, "FR", YEAR).freeFrom()).isEqualTo(YearRange.exactly(2034));
        assertThat(engine.verdict(littlePrince, "US", YEAR).freeFrom()).isEqualTo(YearRange.exactly(2039));
        assertThat(engine.statusInAll(littlePrince, PRODUCTION, YEAR)).isEqualTo(FREE);
    }

    @Test
    void anOldBookByAnAuthorWhoDiedRecentlyIsFreeInTheUnitedStatesOnly() {
        RightsFacts styles = work(1920, AuthorFacts.of("Agatha Christie", 1890, 1976));

        assertThat(status(styles, "US")).isEqualTo(FREE);
        assertThat(engine.verdict(styles, "DE", YEAR).freeFrom()).isEqualTo(YearRange.exactly(2047));
        assertThat(engine.statusInAll(styles, PRODUCTION, YEAR)).isEqualTo(PROTECTED);
    }

    @Test
    void aBookByAnAuthorWhoDiedLongAgoButWasPublishedLateIsFreeInEuropeOnly() {
        RightsFacts nineteenEightyFour = work(1949, AuthorFacts.of("George Orwell", 1903, 1950));

        assertThat(status(nineteenEightyFour, "DE")).isEqualTo(FREE);
        assertThat(status(nineteenEightyFour, "US")).isEqualTo(PROTECTED);
        assertThat(status(nineteenEightyFour, "MX")).isEqualTo(PROTECTED);
        assertThat(status(nineteenEightyFour, "CN")).isEqualTo(FREE);
    }

    @Test
    void aLivingAuthorIsProtectedEvenWithoutADeathDate() {
        RightsFacts harryPotter = work(1997, AuthorFacts.of("J. K. Rowling", 1965, null));

        assertThat(status(harryPotter, "DE")).isEqualTo(PROTECTED);
        assertThat(engine.verdict(harryPotter, "DE", YEAR).freeFrom().earliest())
                .isEqualTo(2068);
        assertThat(engine.verdict(harryPotter, "DE", YEAR).exactYear()).isNull();
        assertThat(engine.verdict(work(1920, AuthorFacts.of("Agatha Christie", 1890, 1976)), "DE", YEAR)
                        .exactYear())
                .isEqualTo(2047);
    }

    @Test
    void anAuthorBornLongAgoIsFreeWithoutADeathDate() {
        assertThat(status(work(1780, AuthorFacts.of("Someone", 1750, null)), "DE"))
                .isEqualTo(FREE);
    }

    @Test
    void aWorkWithNoRecordedAuthorIsJudgedByItsPublication() {
        assertThat(status(work(-800), "DE")).isEqualTo(FREE);
        assertThat(status(work(2001), "DE")).isEqualTo(PROTECTED);
        assertThat(status(work(1930), "DE")).isEqualTo(UNKNOWN);
        assertThat(status(work(1850), "DE")).isEqualTo(FREE);
        assertThat(status(work(null), "DE")).isEqualTo(UNKNOWN);
    }

    @Test
    void aMissingDeathYearThatCouldGoEitherWayIsUnknown() {
        RightsFacts facts = work(1925, AuthorFacts.of("Unknown dates", 1890, null));

        assertThat(status(facts, "DE")).isEqualTo(UNKNOWN);
        assertThat(engine.statusInAll(facts, PRODUCTION, YEAR)).isEqualTo(UNKNOWN);
    }

    @Test
    void aWorkIsFreeOnlyWhenItsLastAuthorsTermHasRunOut() {
        RightsFacts coWritten =
                work(1900, AuthorFacts.of("Died early", 1850, 1901), AuthorFacts.of("Died late", 1880, 1960));

        assertThat(engine.verdict(coWritten, "DE", YEAR).freeFrom()).isEqualTo(YearRange.exactly(2031));
    }

    @Test
    void ukraineCountsFromRehabilitationForARepressedAuthor() {
        RightsFacts cityByPidmohylny = work(1928, new AuthorFacts("Valerian Pidmohylny", 1901, 1937, false, 1956));

        assertThat(status(cityByPidmohylny, "DE")).isEqualTo(FREE);
        assertThat(engine.verdict(cityByPidmohylny, "UA", YEAR).freeFrom()).isEqualTo(YearRange.exactly(2027));
        assertThat(engine.statusInAll(cityByPidmohylny, PRODUCTION, YEAR)).isEqualTo(PROTECTED);
    }

    @Test
    void aRepressedAuthorWithoutARecordedRehabilitationIsUnknownInUkraineButFreeInGermany() {
        RightsFacts zerov = work(1924, new AuthorFacts("Mykola Zerov", 1890, 1937, false, null, true));

        assertThat(status(zerov, "DE")).isEqualTo(FREE);
        assertThat(engine.verdict(zerov, "UA", YEAR).freeFrom()).isEqualTo(new YearRange(2008, 2062));
        assertThat(status(zerov, "UA")).isEqualTo(UNKNOWN);
        assertThat(engine.statusInAll(zerov, PRODUCTION, YEAR)).isEqualTo(UNKNOWN);
    }

    @Test
    void spainKeepsLifePlusEightyForAuthorsWhoDiedBefore1988() {
        assertThat(status(work(1930, AuthorFacts.of("Died 1950", 1880, 1950)), "ES"))
                .isEqualTo(PROTECTED);
        assertThat(status(work(1930, AuthorFacts.of("Died 1940", 1880, 1940)), "ES"))
                .isEqualTo(FREE);
    }

    @Test
    void canadaDidNotReviveWorksWhoseTermHadRunOut() {
        assertThat(status(work(1950, AuthorFacts.of("Died 1965", 1900, 1965)), "CA"))
                .isEqualTo(FREE);
        assertThat(status(work(1950, AuthorFacts.of("Died 1975", 1900, 1975)), "CA"))
                .isEqualTo(PROTECTED);
    }

    @Test
    void russiaAddsFourYearsForAuthorsAliveDuringTheWar() {
        assertThat(engine.verdict(work(1930, AuthorFacts.of("Died 1953", 1880, 1953)), "RU", YEAR)
                        .freeFrom())
                .isEqualTo(YearRange.exactly(2028));
    }

    @Test
    void franceKeepsTheOldTermPlusThirtyAndTheWarYearsForAnAuthorWhoDiedForFrance() {
        AuthorFacts alainFournier = new AuthorFacts("Alain-Fournier", 1886, 1914, true, null);

        assertThat(engine.verdict(work(1913, alainFournier), "FR", YEAR).freeFrom())
                .isEqualTo(YearRange.exactly(1914 + 50 + 30 + 9 + 7 + 1));
        assertThat(engine.verdict(work(1913, AuthorFacts.of("Not for France", 1886, 1914)), "FR", YEAR)
                        .freeFrom())
                .isEqualTo(YearRange.exactly(1985));
    }

    @Test
    void russiaDidNotReviveWorksWhoseOldTermHadRunOutBy1993() {
        assertThat(status(work(1910, AuthorFacts.of("Died 1935", 1870, 1935)), "RU"))
                .isEqualTo(FREE);
        assertThat(engine.verdict(work(1910, AuthorFacts.of("Died 1935", 1870, 1935)), "RU", YEAR)
                        .freeFrom())
                .isEqualTo(YearRange.exactly(1986));
    }

    @Test
    void anUnlistedCountryUsesLifePlusSeventy() {
        assertThat(CountryTerms.of("zz").freeFrom(1950, 1930, null)).isEqualTo(2021);
        assertThat(CountryTerms.countries()).contains("DE", "UA", "FR", "US");
    }
}
