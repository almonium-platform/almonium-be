package com.almonium.learning.rights.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.almonium.learning.catalog.model.CatalogAuthor;
import com.almonium.learning.catalog.model.entity.CatalogWork;
import com.almonium.learning.catalog.service.CatalogMatcher;
import com.almonium.learning.rights.engine.CopyrightEngine;
import com.almonium.learning.rights.exception.ProductionBlockedException;
import com.almonium.learning.rights.model.RightsStatus;
import com.almonium.learning.rights.model.entity.RightsOverride;
import com.almonium.learning.rights.model.enums.RightsEffect;
import com.almonium.learning.rights.repository.RightsOverrideRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ProductionGateTest {
    CatalogMatcher matcher = mock(CatalogMatcher.class);
    RightsOverrideRepository overrides = mock(RightsOverrideRepository.class);
    ProductionGate gate = new ProductionGate(
            new ProductionRights(
                    new CopyrightEngine(),
                    Clock.fixed(Instant.parse("2026-10-06T00:00:00Z"), ZoneOffset.UTC),
                    List.of("UA", "DE")),
            matcher,
            overrides);

    static CatalogWork work(String title, int published, CatalogAuthor author) {
        CatalogWork work = new CatalogWork();
        work.setId(UUID.randomUUID());
        work.setTitle(title);
        work.setPublicationYear(published);
        work.setAuthors(List.of(author));
        work.setAuthorDisplay(author.name());
        return work;
    }

    @Test
    void anOldBookByAnAuthorWhoDiedRecentlyMayNotBePrepared() {
        when(matcher.linkOrMatch("styles", "The Mysterious Affair at Styles", "Agatha Christie"))
                .thenReturn(Optional.of(work(
                        "The Mysterious Affair at Styles",
                        1920,
                        new CatalogAuthor("Agatha Christie", "Q35064", 1890, 1976, false, null))));

        var check = gate.check("styles", "The Mysterious Affair at Styles", "Agatha Christie", 1920);

        assertThat(check.status()).isEqualTo(RightsStatus.PROTECTED);
        assertThat(check.freeFrom()).isEqualTo(2047);
        assertThat(check.explanation()).contains("UA and DE").contains("until the end of 2046");
        assertThatThrownBy(() ->
                        gate.requirePublishable("styles", "The Mysterious Affair at Styles", "Agatha Christie", 1920))
                .isInstanceOf(ProductionBlockedException.class);
    }

    @Test
    void anOpsAllowanceForBothCountriesLetsItThrough() {
        when(matcher.linkOrMatch("styles", "Styles", "Agatha Christie"))
                .thenReturn(Optional.of(
                        work("Styles", 1920, new CatalogAuthor("Agatha Christie", null, 1890, 1976, false, null))));
        RightsOverride everywhere = new RightsOverride();
        everywhere.setCountry("*");
        everywhere.setEffect(RightsEffect.ALLOW);
        when(overrides.findByWorkSlug("styles")).thenReturn(List.of(everywhere));

        assertThat(gate.check("styles", "Styles", "Agatha Christie", 1920).status())
                .isEqualTo(RightsStatus.FREE);
    }

    @Test
    void aWorkTheCatalogueDoesNotKnowIsJudgedByItsOwnYearAndPassesWhenUnsure() {
        when(matcher.linkOrMatch("new", "A Novel", "Nobody Known")).thenReturn(Optional.empty());

        var old = gate.check("new", "A Novel", "Nobody Known", 1850);
        var unsure = gate.check("new", "A Novel", "Nobody Known", 1930);

        assertThat(old.status()).isEqualTo(RightsStatus.FREE);
        assertThat(unsure.status()).isEqualTo(RightsStatus.UNKNOWN);
        assertThat(unsure.explanation()).contains("the edition's own details only");
    }
}
