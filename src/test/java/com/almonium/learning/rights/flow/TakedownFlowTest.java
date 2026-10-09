package com.almonium.learning.rights.flow;

import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.config.PostgresContainer;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.catalog.model.CatalogAuthor;
import com.almonium.learning.catalog.repository.CatalogStore;
import com.almonium.learning.catalog.repository.CatalogStore.WorkRow;
import com.almonium.learning.catalog.service.CatalogMatcher;
import com.almonium.learning.rights.dto.request.RightsOverrideRequest;
import com.almonium.learning.rights.engine.CopyrightEngine;
import com.almonium.learning.rights.model.RightsStatus;
import com.almonium.learning.rights.model.entity.RightsReport;
import com.almonium.learning.rights.model.enums.ReporterRole;
import com.almonium.learning.rights.model.enums.RightsEffect;
import com.almonium.learning.rights.model.enums.RightsReportStatus;
import com.almonium.learning.rights.repository.RightsReportRepository;
import com.almonium.learning.rights.service.ProductionGate;
import com.almonium.learning.rights.service.ProductionRights;
import com.almonium.learning.rights.service.RightsOperationsService;
import com.almonium.learning.rights.service.WorkRightsService;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * The /ops path end to end against PostgreSQL: a shelf work linked to its catalogue work, a takedown everywhere that
 * hides it, the report it answers settled, and the takedown lifted again.
 */
@DataJpaTest
@ImportTestcontainers(PostgresContainer.class)
@Import({
    CopyrightEngine.class,
    ProductionRights.class,
    WorkRightsService.class,
    RightsOperationsService.class,
    ProductionGate.class,
    CatalogStore.class,
    CatalogMatcher.class,
    TakedownFlowTest.Beans.class
})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class TakedownFlowTest {
    @TestConfiguration
    @EnableJpaAuditing
    static class Beans {
        @Bean
        Clock clock() {
            return Clock.fixed(Instant.parse("2026-10-06T00:00:00Z"), ZoneOffset.UTC);
        }

        @Bean
        ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

    @Autowired
    BookRepository books;

    @Autowired
    CatalogStore catalogStore;

    @Autowired
    WorkRightsService workRights;

    @Autowired
    RightsOperationsService operations;

    @Autowired
    ProductionGate productionGate;

    @Autowired
    RightsReportRepository reports;

    final String slug = "dracula-" + UUID.randomUUID();
    final String wikidataId = "Q" + (System.nanoTime() % 1_000_000_000L);

    @BeforeEach
    void aPublishedWorkLinkedToTheCatalogue() {
        Book book = new Book();
        book.setEditionSlug(slug + "-en");
        book.setWorkSlug(slug);
        book.setSourceHash("a".repeat(64));
        book.setTitle("Dracula");
        book.setAuthor("Bram Stoker");
        book.setPublicationYear(1897);
        book.setLanguage(Language.EN);
        book.setOriginalLanguage(Language.EN);
        book.setCefrLevel(CEFR.C1);
        book.setEditionType("original");
        books.saveAndFlush(book);
        UUID work = UUID.randomUUID();
        catalogStore.upsertWorks(
                List.of(new WorkRow(
                        work,
                        wikidataId,
                        null,
                        "Dracula",
                        null,
                        "en",
                        1897,
                        "Bram Stoker",
                        List.of(new CatalogAuthor("Bram Stoker", "Q36184", 1847, 1912, false, null)),
                        120,
                        RightsStatus.FREE,
                        1983)),
                Instant.now());
        catalogStore.linkWorkSlug(work, slug);
        workRights.recompute(slug);
    }

    @Test
    void aTakedownEverywhereHidesTheWorkSettlesTheReportAndLiftsAgain() {
        assertThat(workRights.readable(slug, "DE")).isTrue();
        assertThat(workRights.readable(slug, "US")).isTrue();
        assertThat(operations.work(slug).wikidataId()).isEqualTo(wikidataId);

        RightsReport report = new RightsReport();
        report.setBook(books.findByWorkSlug(slug).getFirst());
        report.setWorkSlug(slug);
        report.setReporterEmail("rights@example.com");
        report.setReporterRole(ReporterRole.RIGHTS_HOLDER);
        report.setMessage("We hold the rights to this edition's source.");
        report.setStatus(RightsReportStatus.OPEN);
        report = reports.saveAndFlush(report);

        var override = operations.override(
                null,
                new RightsOverrideRequest(slug, "*", RightsEffect.BLOCK, "Rights holder's notice", report.getId()));

        assertThat(workRights.readable(slug, "DE")).isFalse();
        assertThat(workRights.readable(slug, "UA")).isFalse();
        assertThat(reports.findById(report.getId()).orElseThrow().getStatus()).isEqualTo(RightsReportStatus.ACTIONED);
        assertThat(operations.works())
                .filteredOn(row -> row.workSlug().equals(slug))
                .singleElement()
                .satisfies(row -> assertThat(row.blockedEverywhere()).isTrue());
        assertThat(operations.work(slug).blockedCountries()).containsEntry("*", "OVERRIDE");

        workRights.reload();
        assertThat(workRights.readable(slug, "DE")).isFalse();

        operations.removeOverride(override.id());
        assertThat(workRights.readable(slug, "DE")).isTrue();
    }

    @Test
    void aCountryTakedownHidesTheWorkThereOnlyAndThePublishCheckReadsTheLinkedFacts() {
        operations.override(null, new RightsOverrideRequest(slug, "pl", RightsEffect.BLOCK, "Polish publisher", null));

        assertThat(workRights.readable(slug, "PL")).isFalse();
        assertThat(workRights.readable(slug, "DE")).isTrue();
        assertThat(productionGate.check(slug, "Dracula", "Bram Stoker", 1897).status())
                .isEqualTo(RightsStatus.FREE);
    }
}
