package com.almonium.learning.rights.service;

import com.almonium.learning.rights.engine.CopyrightEngine;
import com.almonium.learning.rights.engine.CountryTerms;
import com.almonium.learning.rights.model.CountryVerdict;
import com.almonium.learning.rights.model.RightsFacts;
import com.almonium.learning.rights.model.RightsStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * When a work becomes free to read, country by country (W6, W7): the unlock map's data, the day a reader's own country
 * frees it, and the places it is already free. A reader can open a book only once it is free where editions are made
 * and in their own country, so the reader's date is the later of the two; it is given only when both are exact, the
 * one case a countdown may count to.
 */
@Service
@RequiredArgsConstructor
public class UnlockDates {
    /** Countries named first in "already free in", by how many readers they are likely to hold. */
    static final List<String> NAMED_FIRST =
            List.of("US", "GB", "CA", "AU", "IN", "JP", "BR", "MX", "KR", "TR", "AR", "NZ", "ZA", "PH", "EG");
    /** A countdown is shown only this close to the day; further out the card names the date. */
    public static final int COUNTDOWN_YEARS = 5;

    private final CopyrightEngine engine;
    private final ProductionRights productionRights;

    /** One country's answer for a work: its status this year, the year it frees up when known, and the rule. */
    public record Unlock(String country, RightsStatus status, Integer year, String rule) {}

    /** Every country the JDK knows, alphabetically by code. */
    public List<Unlock> everywhere(RightsFacts facts) {
        int year = productionRights.currentYear();
        return CountryTerms.countries().stream()
                .sorted()
                .map(country -> unlock(facts, country, year))
                .toList();
    }

    public Unlock in(RightsFacts facts, String country) {
        return unlock(facts, country, productionRights.currentYear());
    }

    /**
     * The year the reader can read the work: free where it is made and in the reader's country, whichever is later.
     * Null unless both years are exact, or when the work is already free in both.
     */
    public Integer readerFreeFrom(RightsFacts facts, String country) {
        int year = productionRights.currentYear();
        Integer production = productionRights.verdict(facts, year).exactYear();
        Integer reader =
                engine.verdict(facts, country.toUpperCase(Locale.ROOT), year).exactYear();
        if (production == null || reader == null) {
            return null;
        }
        return Math.max(production, reader);
    }

    /** Whether the reader's date is close enough for the card to count down to it (under five years away). */
    public boolean countsDown(Integer readerFreeFrom) {
        int year = productionRights.currentYear();
        return readerFreeFrom != null && readerFreeFrom > year && readerFreeFrom <= year + COUNTDOWN_YEARS;
    }

    /** Up to {@code max} countries where the work is free today, the best-known first, not counting the reader's. */
    public List<String> alreadyFreeIn(RightsFacts facts, String readerCountry, int max) {
        int year = productionRights.currentYear();
        List<String> free = new ArrayList<>();
        for (String country : NAMED_FIRST) {
            if (free.size() == max) {
                break;
            }
            if (country.equalsIgnoreCase(readerCountry)) {
                continue;
            }
            if (engine.verdict(facts, country, year).status() == RightsStatus.FREE) {
                free.add(country);
            }
        }
        return free;
    }

    private Unlock unlock(RightsFacts facts, String country, int year) {
        CountryVerdict verdict = engine.verdict(facts, country, year);
        return new Unlock(country, verdict.status(), verdict.exactYear(), CountryTerms.rule(country));
    }
}
