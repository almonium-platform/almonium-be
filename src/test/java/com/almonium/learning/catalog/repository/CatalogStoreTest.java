package com.almonium.learning.catalog.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.config.PostgresContainer;
import com.almonium.learning.catalog.model.CatalogAuthor;
import com.almonium.learning.catalog.repository.CatalogStore.Hit;
import com.almonium.learning.catalog.repository.CatalogStore.TitleRow;
import com.almonium.learning.catalog.repository.CatalogStore.WorkRow;
import com.almonium.learning.catalog.service.TitleFolding;
import com.almonium.learning.rights.model.RightsStatus;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import tools.jackson.databind.ObjectMapper;

/** The trigram search and the bulk writes, against PostgreSQL with pg_trgm as Liquibase sets it up. */
@DataJpaTest
@ImportTestcontainers(PostgresContainer.class)
class CatalogStoreTest {
    @Autowired
    JdbcTemplate jdbc;

    CatalogStore store;
    final List<WorkRow> works = new ArrayList<>();
    final List<TitleRow> titles = new ArrayList<>();

    @BeforeEach
    void setUp() {
        store = new CatalogStore(jdbc, new NamedParameterJdbcTemplate(jdbc), new ObjectMapper());
        work("Dracula", "Bram Stoker", 1897, 120, RightsStatus.FREE, "Dracula", "Дракула");
        work("Bram Stoker's Dracula", "James V. Hart", 1992, 30, RightsStatus.PROTECTED, "Bram Stoker's Dracula");
        work(
                "The Hound of the Baskervilles",
                "Arthur Conan Doyle",
                1902,
                90,
                RightsStatus.FREE,
                "The Hound of the Baskervilles",
                "Der Hund von Baskerville");
        work("A Study in Scarlet", "Arthur Conan Doyle", 1887, 70, RightsStatus.FREE, "A Study in Scarlet");
        work("Bahnwärter Thiel", "Gerhart Hauptmann", 1888, 20, RightsStatus.FREE, "Bahnwärter Thiel");
        store.upsertWorks(works, Instant.now());
        store.replaceTitles(titles);
    }

    void work(String title, String author, int year, int popularity, RightsStatus status, String... names) {
        UUID id = UUID.randomUUID();
        works.add(new WorkRow(
                id,
                "Q" + works.size(),
                null,
                title,
                null,
                null,
                year,
                author,
                List.of(new CatalogAuthor(author, null, null, null, false, null)),
                popularity,
                status,
                null));
        for (String name : names) {
            titles.add(new TitleRow(id, name, null, "TITLE", TitleFolding.fold(name)));
        }
        titles.add(new TitleRow(id, author, null, "AUTHOR", TitleFolding.fold(author)));
        titles.add(new TitleRow(id, title + " " + author, null, "FULL", TitleFolding.fold(title + " " + author)));
    }

    List<String> search(String query) {
        return store.search(TitleFolding.fold(query), 10).stream()
                .map(Hit::title)
                .toList();
    }

    @Test
    void anExactTitleComesFirstThenTheBetterKnownWork() {
        assertThat(search("dracula")).startsWith("Dracula", "Bram Stoker's Dracula");
        assertThat(search("dracula stoker").getFirst()).isEqualTo("Dracula");
    }

    @Test
    void findsWorksByAuthorByAnotherLanguagesTitleAndWithoutAccents() {
        assertThat(search("conan doyle")).containsExactly("The Hound of the Baskervilles", "A Study in Scarlet");
        assertThat(search("hund von baskerville")).containsExactly("The Hound of the Baskervilles");
        assertThat(search("bahnwarter")).containsExactly("Bahnwärter Thiel");
        assertThat(search("дракула")).contains("Dracula");
    }

    @Test
    void anImportKeepsTheShelfLinkAndReplacesTitles() {
        UUID dracula = works.getFirst().id();
        store.linkWorkSlug(dracula, "dracula");
        store.upsertWorks(works, Instant.now());
        store.replaceTitles(titles);

        assertThat(store.isLinked("dracula")).isTrue();
        assertThat(store.exactTitle("dracula")).extracting(Hit::workSlug).containsExactly("dracula");
        assertThat(jdbc.queryForObject("select count(*) from catalog_title", Integer.class))
                .isEqualTo(titles.size());
    }
}
