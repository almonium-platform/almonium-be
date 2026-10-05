package com.almonium.learning.rights.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.catalog.model.CatalogAuthor;
import com.almonium.learning.catalog.model.entity.CatalogWork;
import com.almonium.learning.catalog.repository.CatalogWorkRepository;
import com.almonium.learning.rights.engine.CopyrightEngine;
import com.almonium.learning.rights.model.entity.RightsOverride;
import com.almonium.learning.rights.model.enums.BlockSource;
import com.almonium.learning.rights.model.enums.RightsEffect;
import com.almonium.learning.rights.repository.RightsOverrideRepository;
import com.almonium.learning.rights.repository.WorkRightsBlockRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionTemplate;

class WorkRightsServiceTest {
    static final Clock OCTOBER_2026 = Clock.fixed(Instant.parse("2026-10-06T00:00:00Z"), ZoneOffset.UTC);

    CatalogWorkRepository catalogWorks = mock(CatalogWorkRepository.class);
    BookRepository books = mock(BookRepository.class);
    RightsOverrideRepository overrides = mock(RightsOverrideRepository.class);
    WorkRightsService service;

    @BeforeEach
    void setUp() {
        CopyrightEngine engine = new CopyrightEngine();
        service = new WorkRightsService(
                engine,
                new ProductionRights(engine, OCTOBER_2026, List.of("UA", "DE")),
                catalogWorks,
                books,
                overrides,
                mock(WorkRightsBlockRepository.class),
                mock(TransactionTemplate.class));
    }

    void catalogue(String slug, Integer published, CatalogAuthor author) {
        CatalogWork work = new CatalogWork();
        work.setWorkSlug(slug);
        work.setPublicationYear(published);
        work.setAuthors(List.of(author));
        when(catalogWorks.findByWorkSlug(slug)).thenReturn(List.of(work));
    }

    RightsOverride override(String slug, String country, RightsEffect effect) {
        RightsOverride override = new RightsOverride();
        override.setWorkSlug(slug);
        override.setCountry(country);
        override.setEffect(effect);
        return override;
    }

    @Test
    void theLittlePrinceIsHiddenInFranceAndTheUnitedStatesOnly() {
        catalogue(
                "the-little-prince",
                1943,
                new CatalogAuthor("Antoine de Saint-Exupéry", "Q2908", 1900, 1944, true, null));

        Map<String, BlockSource> blocks = service.computeBlocks("the-little-prince");

        assertThat(blocks).containsEntry("FR", BlockSource.ENGINE).containsEntry("US", BlockSource.ENGINE);
        assertThat(blocks).doesNotContainKeys("*", "DE", "UA", "PL", "GB");
    }

    @Test
    void aWorkProtectedWhereWeMakeBooksIsHiddenEverywhere() {
        catalogue("styles", 1920, new CatalogAuthor("Agatha Christie", "Q35064", 1890, 1976, false, null));

        assertThat(service.computeBlocks("styles")).containsEntry("*", BlockSource.ENGINE);
    }

    @Test
    void aTakedownHidesTheWorkAndAnAllowanceShowsIt() {
        catalogue(
                "the-little-prince",
                1943,
                new CatalogAuthor("Antoine de Saint-Exupéry", "Q2908", 1900, 1944, true, null));
        when(overrides.findByWorkSlug("the-little-prince"))
                .thenReturn(List.of(
                        override("the-little-prince", "PL", RightsEffect.BLOCK),
                        override("the-little-prince", "US", RightsEffect.ALLOW)));

        Map<String, BlockSource> blocks = service.computeBlocks("the-little-prince");

        assertThat(blocks).containsEntry("PL", BlockSource.OVERRIDE).containsEntry("FR", BlockSource.ENGINE);
        assertThat(blocks).doesNotContainKey("US");
    }

    @Test
    void anAllowanceForTheProductionCountriesLiftsTheEverywhereBlock() {
        catalogue("styles", 1920, new CatalogAuthor("Agatha Christie", "Q35064", 1890, 1976, false, null));
        when(overrides.findByWorkSlug("styles"))
                .thenReturn(List.of(
                        override("styles", "UA", RightsEffect.ALLOW), override("styles", "DE", RightsEffect.ALLOW)));

        Map<String, BlockSource> blocks = service.computeBlocks("styles");

        assertThat(blocks).doesNotContainKeys("*", "UA", "DE").containsEntry("FR", BlockSource.ENGINE);
    }

    @Test
    void aTakedownEverywhereWinsOverTheEngine() {
        catalogue("dracula", 1897, new CatalogAuthor("Bram Stoker", "Q36184", 1847, 1912, false, null));
        when(overrides.findByWorkSlug("dracula")).thenReturn(List.of(override("dracula", "*", RightsEffect.BLOCK)));

        assertThat(service.computeBlocks("dracula")).containsExactly(Map.entry("*", BlockSource.OVERRIDE));
    }

    @Test
    void anUnlinkedEditionIsJudgedByItsOwnYearSoOldBooksPassAndTheUnitedStatesStillCounts() {
        Book book = new Book();
        book.setWorkSlug("unlinked");
        book.setTitle("Unlinked");
        book.setAuthor("Somebody");
        book.setPublicationYear(1943);
        when(books.findByWorkSlug("unlinked")).thenReturn(List.of(book));

        Map<String, BlockSource> blocks = service.computeBlocks("unlinked");

        assertThat(blocks).containsKey("US").doesNotContainKeys("*", "DE");
    }

    @Test
    void readableUnlessHiddenInTheCountryOrEverywhere() {
        assertThat(service.readable("anything", "DE")).isTrue();
        assertThat(service.readable(null, "DE")).isTrue();
    }
}
