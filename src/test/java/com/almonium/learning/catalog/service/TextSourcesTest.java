package com.almonium.learning.catalog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.almonium.learning.catalog.dto.response.SourceOption;
import com.almonium.learning.catalog.dto.response.TextSourceView;
import com.almonium.learning.catalog.model.CatalogAuthor;
import com.almonium.learning.catalog.model.entity.CatalogWork;
import com.almonium.learning.catalog.repository.CatalogStore;
import com.almonium.learning.catalog.repository.CatalogStore.SourceRow;
import com.almonium.learning.catalog.repository.CatalogWorkRepository;
import com.almonium.learning.rights.engine.CopyrightEngine;
import com.almonium.learning.rights.model.RightsStatus;
import com.almonium.learning.rights.service.ProductionRights;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class TextSourcesTest {
    CatalogStore store = mock(CatalogStore.class);
    CatalogWorkRepository works = mock(CatalogWorkRepository.class);
    TextSources sources = new TextSources(
            store,
            works,
            new ProductionRights(
                    new CopyrightEngine(),
                    Clock.fixed(Instant.parse("2026-10-08T00:00:00Z"), ZoneOffset.UTC),
                    List.of("UA", "DE")),
            new SourceOrder("en=STANDARD_EBOOKS,GUTENBERG,WIKISOURCE;*=GUTENBERG,WIKISOURCE"));

    static CatalogWork warAndPeace() {
        CatalogWork work = new CatalogWork();
        work.setId(UUID.randomUUID());
        work.setTitle("War and Peace");
        work.setPublicationYear(1869);
        work.setOriginalLanguage("ru");
        work.setAuthors(List.of(new CatalogAuthor("Leo Tolstoy", "Q7243", 1828, 1910, false, null)));
        return work;
    }

    static SourceRow source(String kind, String language, String reference, boolean translation, CatalogAuthor... by) {
        return new SourceRow(
                UUID.randomUUID(),
                UUID.randomUUID(),
                kind,
                language,
                reference,
                "label",
                translation,
                List.of(by),
                true);
    }

    @Test
    void anOriginalIsAsFreeAsItsWork() {
        var view = sources.view(
                warAndPeace(),
                source("WIKISOURCE", "ru", "https://ru.wikisource.org/wiki/Война_и_мир_(Толстой)", false));

        assertThat(view.status()).isEqualTo(RightsStatus.FREE);
        assertThat(view.downloadUrl())
                .startsWith("https://ws-export.wmcloud.org/?lang=ru&page=")
                .endsWith("&format=epub-3");
    }

    @Test
    void aTranslationIsFreeOnlyWhenItsTranslatorsAreToo() {
        CatalogWork work = warAndPeace();
        var maude = sources.view(
                work,
                source(
                        "GUTENBERG",
                        "en",
                        "2600",
                        true,
                        new CatalogAuthor("Aylmer Maude", null, 1858, 1938, false, null)));
        var recent = sources.view(
                work,
                source(
                        "GUTENBERG",
                        "en",
                        "9999",
                        true,
                        new CatalogAuthor("Translator died 1999", null, 1920, 1999, false, null)));
        var undated = sources.view(
                work,
                source(
                        "GUTENBERG",
                        "en",
                        "9998",
                        true,
                        new CatalogAuthor("Translator alive or undated", null, 1943, null, false, null)));
        var nobodyKnown = sources.view(work, source("WIKISOURCE", "ko", "https://ko.wikisource.org/wiki/x", true));

        assertThat(maude.status()).isEqualTo(RightsStatus.FREE);
        assertThat(maude.downloadUrl()).isEqualTo("https://www.gutenberg.org/ebooks/2600.epub3.images");
        assertThat(maude.pageUrl()).isEqualTo("https://www.gutenberg.org/ebooks/2600");
        assertThat(recent.status()).isEqualTo(RightsStatus.PROTECTED);
        assertThat(recent.freeFrom()).isEqualTo(2070);
        assertThat(undated.status()).isEqualTo(RightsStatus.UNKNOWN);
        assertThat(nobodyKnown.status()).isEqualTo(RightsStatus.UNKNOWN);
    }

    @Test
    void aWorkUnderCopyrightMakesEveryTextOfItProtected() {
        CatalogWork styles = new CatalogWork();
        styles.setId(UUID.randomUUID());
        styles.setPublicationYear(1920);
        styles.setAuthors(List.of(new CatalogAuthor("Agatha Christie", null, 1890, 1976, false, null)));
        when(works.findById(styles.getId())).thenReturn(Optional.of(styles));
        when(store.sources(styles.getId())).thenReturn(List.of(source("GUTENBERG", "en", "863", false)));

        assertThat(sources.forWork(styles.getId())).singleElement().satisfies(view -> {
            assertThat(view.status()).isEqualTo(RightsStatus.PROTECTED);
            assertThat(view.freeFrom()).isEqualTo(2047);
        });
    }

    @Test
    void sourcesAreRankedByRightsThenShapeThenTheLanguagesLibraryOrder() {
        CatalogWork work = warAndPeace();
        work.setOriginalLanguage("en");
        SourceRow gutenberg = source("GUTENBERG", "en", "2600", false);
        SourceRow standard = source("STANDARD_EBOOKS", "en", "/ebooks/leo-tolstoy/war-and-peace", false);
        SourceRow suspect = source("STANDARD_EBOOKS", "en", "/ebooks/leo-tolstoy/war-and-peace-2", false);
        SourceRow measured = source("GUTENBERG", "en", "2601", false);
        when(works.findById(work.getId())).thenReturn(Optional.of(work));
        when(store.sources(work.getId())).thenReturn(List.of(gutenberg, standard, suspect, measured));
        when(store.shapes(any()))
                .thenReturn(Map.of(
                        TextSources.downloadUrl(suspect), "suspect",
                        TextSources.downloadUrl(measured), "ok"));

        List<TextSourceView> ranked = sources.forWork(work.getId());

        // the English order prefers Standard Ebooks; among Gutenberg's files the measured one comes first; a file
        // the processor found suspect sinks below every sound or unmeasured one
        assertThat(ranked.stream().map(TextSourceView::id).toList())
                .containsExactly(standard.id(), measured.id(), gutenberg.id(), suspect.id());
        assertThat(ranked.getFirst().downloadUrl())
                .isEqualTo("https://standardebooks.org/ebooks/leo-tolstoy/war-and-peace/downloads/"
                        + "leo-tolstoy_war-and-peace.epub?source=download");
        assertThat(ranked.getFirst().pageUrl())
                .isEqualTo("https://standardebooks.org/ebooks/leo-tolstoy/war-and-peace");
        assertThat(ranked.get(1).shape()).isEqualTo("ok");
    }

    @Test
    void wolneLekturyIsReferencedBySlug() {
        SourceRow row = source("WOLNE_LEKTURY", "pl", "balzac-gabinet-starozytnosci", true);
        assertThat(TextSources.pageUrl(row))
                .isEqualTo("https://wolnelektury.pl/katalog/lektura/balzac-gabinet-starozytnosci/");
        assertThat(TextSources.downloadUrl(row))
                .isEqualTo("https://wolnelektury.pl/media/book/epub/balzac-gabinet-starozytnosci.epub");
    }

    @Test
    void theOptionsMarkTheFirstFreeTextAsTheDefaultAndListProtectedOnesToo() {
        CatalogWork work = warAndPeace();
        work.setOriginalLanguage("ru");
        SourceRow original = source("GUTENBERG", "ru", "2600", false);
        SourceRow recent = source(
                "GUTENBERG", "en", "2601", true, new CatalogAuthor("Living Translator", null, 1960, null, false, null));
        when(works.findById(work.getId())).thenReturn(Optional.of(work));
        when(store.sources(work.getId())).thenReturn(List.of(recent, original));
        when(store.shapes(any())).thenReturn(Map.of());

        var options = sources.options(work.getId());

        assertThat(options).extracting(SourceOption::preferred).containsExactly(true, false);
        assertThat(options.getFirst().downloadUrl()).isEqualTo("https://www.gutenberg.org/ebooks/2600.epub3.images");
        assertThat(options.get(1).status()).isEqualTo(RightsStatus.PROTECTED);
        assertThat(sources.byDownloadUrl(work.getId(), "https://www.gutenberg.org/ebooks/2600.epub3.images"))
                .isPresent();
        assertThat(sources.byDownloadUrl(work.getId(), "https://elsewhere/x.epub"))
                .isEmpty();
    }
}
