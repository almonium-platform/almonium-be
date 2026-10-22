package com.almonium.learning.rights.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.learning.rights.engine.CopyrightEngine;
import com.almonium.learning.rights.model.AuthorFacts;
import com.almonium.learning.rights.model.RightsFacts;
import com.almonium.learning.rights.model.RightsStatus;
import com.almonium.learning.rights.service.UnlockDates.Unlock;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;

class UnlockDatesTest {
    final Clock clock = Clock.fixed(Instant.parse("2026-10-22T12:00:00Z"), ZoneOffset.UTC);
    final UnlockDates unlockDates = new UnlockDates(
            new CopyrightEngine(), new ProductionRights(new CopyrightEngine(), clock, List.of("UA", "DE")));

    /** Winnie-the-Pooh: published 1926, A. A. Milne died 1956. */
    final RightsFacts pooh = new RightsFacts(1926, List.of(AuthorFacts.of("A. A. Milne", 1882, 1956)));

    @Test
    void aReaderInGermanyGetsTheBookOnTheFirstOfJanuary2027() {
        assertThat(unlockDates.readerFreeFrom(pooh, "DE")).isEqualTo(2027);
        assertThat(unlockDates.countsDown(2027)).isTrue();
        assertThat(unlockDates.countsDown(2032)).isFalse();
        assertThat(unlockDates.countsDown(null)).isFalse();
    }

    @Test
    void aReaderInTheUnitedStatesStillWaitsForTheCountriesWhereBooksAreMade() {
        // Free in the US since 2022, but an edition can only be made once Ukraine and Germany free it.
        assertThat(unlockDates.readerFreeFrom(pooh, "US")).isEqualTo(2027);
        assertThat(unlockDates.readerFreeFrom(pooh, "MX")).isEqualTo(2057);
    }

    @Test
    void theDateIsOnlyGivenWhenTheFactsPinItDown() {
        RightsFacts unknownDeath = new RightsFacts(1926, List.of(AuthorFacts.of("Someone", 1900, null)));
        assertThat(unlockDates.readerFreeFrom(unknownDeath, "DE")).isNull();
    }

    @Test
    void theMapNamesEveryCountryWithItsYearAndRule() {
        List<Unlock> all = unlockDates.everywhere(pooh);
        assertThat(all).hasSize(CountryTermsCount.COUNTRIES);
        assertThat(all).extracting(Unlock::country).isSorted();
        assertThat(all.stream()
                        .filter(u -> u.country().equals("DE"))
                        .findFirst()
                        .orElseThrow())
                .isEqualTo(new Unlock("DE", RightsStatus.PROTECTED, 2027, "Author's life + 70 years"));
        assertThat(all.stream()
                        .filter(u -> u.country().equals("US"))
                        .findFirst()
                        .orElseThrow()
                        .status())
                .isEqualTo(RightsStatus.FREE);
        assertThat(all.stream()
                        .filter(u -> u.country().equals("CA"))
                        .findFirst()
                        .orElseThrow()
                        .year())
                .isEqualTo(2007);
    }

    @Test
    void alreadyFreeInNamesTheBestKnownCountriesButNotTheReadersOwn() {
        assertThat(unlockDates.alreadyFreeIn(pooh, "DE", 3)).containsExactly("US", "CA", "IN");
        assertThat(unlockDates.alreadyFreeIn(pooh, "US", 3)).containsExactly("CA", "IN", "JP");
    }

    static final class CountryTermsCount {
        static final int COUNTRIES =
                com.almonium.learning.rights.engine.CountryTerms.countries().size();
    }
}
