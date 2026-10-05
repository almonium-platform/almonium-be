package com.almonium.learning.rights.service;

import com.almonium.learning.rights.engine.CopyrightEngine;
import com.almonium.learning.rights.model.CountryVerdict;
import com.almonium.learning.rights.model.RightsFacts;
import com.almonium.learning.rights.model.RightsStatus;
import com.almonium.learning.rights.model.YearRange;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Whether a work may be prepared at all: free where editions are made (Ukraine) and where they are hosted (Germany).
 * Making a graded or translated edition is an adaptation, so this is the gate before any reader's country matters.
 */
@Service
public class ProductionRights {
    private final CopyrightEngine engine;
    private final Clock clock;

    @Getter
    private final List<String> countries;

    public ProductionRights(
            CopyrightEngine engine,
            Clock clock,
            @Value("${app.rights.production-countries:UA,DE}") List<String> countries) {
        this.engine = engine;
        this.clock = clock;
        this.countries = List.copyOf(countries);
    }

    public int currentYear() {
        return LocalDate.now(clock).getYear();
    }

    /** The combined answer for every production country: free only when free in each, from the latest of them. */
    public CountryVerdict verdict(RightsFacts facts, int year) {
        Integer earliest = null;
        Integer latest = Integer.MIN_VALUE;
        for (String country : countries) {
            YearRange range = engine.verdict(facts, country, year).freeFrom();
            if (range.earliest() != null) {
                earliest = earliest == null ? range.earliest() : Math.max(earliest, range.earliest());
            }
            latest = latest == null || range.latest() == null ? null : Math.max(latest, range.latest());
        }
        RightsStatus status = engine.statusInAll(facts, countries, year);
        return new CountryVerdict(String.join(",", countries), status, new YearRange(earliest, latest));
    }

    public CountryVerdict verdict(RightsFacts facts) {
        return verdict(facts, currentYear());
    }
}
