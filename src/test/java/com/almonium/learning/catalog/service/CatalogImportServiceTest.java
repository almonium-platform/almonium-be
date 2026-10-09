package com.almonium.learning.catalog.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.learning.catalog.model.CatalogAuthor;
import com.almonium.learning.catalog.repository.CatalogStore.TitleRow;
import com.almonium.learning.catalog.service.GutenbergCatalogSource.Author;
import com.almonium.learning.catalog.service.GutenbergCatalogSource.Text;
import com.almonium.learning.catalog.service.WikidataCatalogSource.Label;
import com.almonium.learning.catalog.service.WikidataCatalogSource.Person;
import com.almonium.learning.catalog.service.WikidataCatalogSource.Snapshot;
import com.almonium.learning.catalog.service.WikidataCatalogSource.Work;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CatalogImportServiceTest {
    static final UUID KEPT = UUID.randomUUID();

    static Snapshot snapshot() {
        return new Snapshot(
                Map.of(
                        "Q25338", new Work("Q25338", 151, "fr", 1943),
                        "Q8337", new Work("Q8337", 70, "en", 1887),
                        "Q1", new Work("Q1", 5, "en", 1887),
                        "Q2", new Work("Q2", 1, null, 1950)),
                Map.of("Q25338", List.of("P1"), "Q8337", List.of("P2"), "Q1", List.of("P2")),
                Map.of(
                        "P1", new Person("P1", "Antoine de Saint-Exupéry", 1900, 1944, true),
                        "P2", new Person("P2", "Arthur Conan Doyle", 1859, null, false)),
                Map.of(
                        "Q25338",
                        List.of(
                                new Label("Q25338", "Le Petit Prince", "fr"),
                                new Label("Q25338", "The Little Prince", "en"),
                                new Label("Q25338", "Der kleine Prinz", "de")),
                        "Q8337",
                        List.of(new Label("Q8337", "A Study in Scarlet", "en")),
                        "Q1",
                        List.of(new Label("Q1", "A Study in Scarlet (film)", "en"))),
                Map.of("Q8337", 244, "Q1", 244));
    }

    @Test
    void takesEveryTitledWorkWithItsAuthorsFactsAndKeepsExistingIds() {
        var records = CatalogImportService.fromWikidata(snapshot(), Map.of("Q25338", KEPT));

        assertThat(records).extracting(record -> record.wikidataId).containsExactlyInAnyOrder("Q25338", "Q8337", "Q1");
        var prince = records.stream()
                .filter(r -> r.wikidataId.equals("Q25338"))
                .findFirst()
                .orElseThrow();
        assertThat(prince.id).isEqualTo(KEPT);
        assertThat(prince.title).isEqualTo("The Little Prince");
        assertThat(prince.originalTitle).isEqualTo("Le Petit Prince");
        assertThat(prince.authors)
                .containsExactly(new CatalogAuthor("Antoine de Saint-Exupéry", "P1", 1900, 1944, true, null));
        assertThat(prince.titles).hasSize(3);
    }

    @Test
    void aGutenbergIdTwoWorksClaimStaysWithTheBetterKnownOne() {
        var records = CatalogImportService.fromWikidata(snapshot(), Map.of());

        assertThat(records.stream().filter(r -> Integer.valueOf(244).equals(r.gutenbergId)))
                .extracting(r -> r.wikidataId)
                .containsExactly("Q8337");
    }

    @Test
    void gutenbergTextsAttachToTheirWorkFillMissingDeathsAndTheRestBecomeWorks() {
        var records = CatalogImportService.fromWikidata(snapshot(), Map.of());
        List<Text> texts = List.of(
                new Text(
                        244,
                        "A Study in Scarlet",
                        "en",
                        List.of(new Author("Arthur Conan Doyle", "Doyle", 1859, 1930))),
                new Text(
                        58876,
                        "Judith Moore; or, Fashioning a Pipe",
                        "en",
                        List.of(new Author("Joanna E. Wood", "Wood", 1867, 1927))),
                new Text(58877, "Judith Moore", "en", List.of(new Author("Joanna E. Wood", "Wood", 1867, 1927))),
                new Text(10, "The Little Prince", "en", List.of(new Author("Someone Else", "Else", 1900, 1950))));

        var added = CatalogImportService.mergeGutenberg(records, texts, Map.of(58876, KEPT));

        var scarlet = records.stream()
                .filter(r -> "Q8337".equals(r.wikidataId))
                .findFirst()
                .orElseThrow();
        assertThat(scarlet.authors.getFirst().deathYear()).isEqualTo(1930);
        assertThat(added).extracting(r -> r.title).containsExactlyInAnyOrder("Judith Moore", "The Little Prince");
        var judith = added.stream()
                .filter(r -> r.title.equals("Judith Moore"))
                .findFirst()
                .orElseThrow();
        assertThat(judith.id).isEqualTo(KEPT);
        assertThat(judith.gutenbergId).isEqualTo(58876);
        assertThat(judith.authors.getFirst().deathYear()).isEqualTo(1927);
    }

    @Test
    void aWorkIsFoundByEachTitleByEachAuthorAndByTitleAndAuthorTogether() {
        var prince = CatalogImportService.fromWikidata(snapshot(), Map.of()).stream()
                .filter(r -> "Q25338".equals(r.wikidataId))
                .findFirst()
                .orElseThrow();

        List<TitleRow> rows = CatalogImportService.titleRows(prince);

        assertThat(rows)
                .extracting(TitleRow::normalized)
                .contains(
                        "le petit prince",
                        "the little prince",
                        "der kleine prinz",
                        "antoine de saint exupery",
                        "the little prince antoine de saint exupery");
        assertThat(rows).extracting(TitleRow::kind).contains("TITLE", "AUTHOR", "FULL");
    }

    @Test
    void sourcesKeepTheirLanguageAndTranslatorsAndAPageInAnotherLanguageIsATranslation() {
        Snapshot base = snapshot();
        Map<String, Person> people = new java.util.HashMap<>(base.people());
        people.put("Q99", new Person("Q99", "Katherine Woods", 1886, 1968, false));
        Snapshot withPages = new Snapshot(
                base.works(),
                base.authorsByWork(),
                people,
                base.labelsByWork(),
                base.gutenbergByWork(),
                Map.of(),
                Map.of(
                        "Q25338",
                        List.of(
                                new WikidataCatalogSource.WikisourcePage(
                                        "https://fr.wikisource.org/wiki/Le_Petit_Prince", "fr", null, List.of()),
                                new WikidataCatalogSource.WikisourcePage(
                                        "https://en.wikisource.org/wiki/The_Little_Prince",
                                        "en",
                                        "Q1",
                                        List.of("Q99")))));

        var prince = CatalogImportService.fromWikidata(withPages, Map.of()).stream()
                .filter(r -> "Q25338".equals(r.wikidataId))
                .findFirst()
                .orElseThrow();

        assertThat(prince.sources).hasSize(2);
        assertThat(prince.sources.get(0).translation()).isFalse();
        assertThat(prince.sources.get(0).label()).isEqualTo("Le Petit Prince");
        assertThat(prince.sources.get(1).translation()).isTrue();
        assertThat(prince.sources.get(1).translators())
                .extracting(CatalogAuthor::deathYear)
                .containsExactly(1968);
    }

    @Test
    void aGutenbergTextWithTranslatorsIsATranslation() {
        var maude = CatalogImportService.gutenbergSource(
                new Text(
                        2600,
                        "War and Peace",
                        "en",
                        List.of(new Author("Leo Tolstoy", "Tolstoy", 1828, 1910)),
                        List.of(new Author("Aylmer Maude", "Maude", 1858, 1938))),
                "ru");
        var original = CatalogImportService.gutenbergSource(
                new Text(84, "Frankenstein; or, the modern prometheus", "en", List.of()), "en");

        assertThat(maude.translation()).isTrue();
        assertThat(maude.translators()).extracting(CatalogAuthor::name).containsExactly("Aylmer Maude");
        assertThat(original.translation()).isFalse();
        assertThat(original.label()).isEqualTo("Frankenstein");
        assertThat(original.reference()).isEqualTo("84");
    }

    @Test
    void englishTitleFirstThenTheMultilingualThenTheOriginal() {
        assertThat(CatalogImportService.pickTitle(
                        List.of(new Label("Q", "Kobzar", "mul"), new Label("Q", "Кобзар", "uk")), "uk"))
                .isEqualTo("Kobzar");
        assertThat(CatalogImportService.pickTitle(List.of(new Label("Q", "Кобзар", "uk")), "uk"))
                .isEqualTo("Кобзар");
        assertThat(CatalogImportService.pickTitle(List.of(), "uk")).isNull();
    }
}
