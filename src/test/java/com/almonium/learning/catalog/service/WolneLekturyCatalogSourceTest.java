package com.almonium.learning.catalog.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

class WolneLekturyCatalogSourceTest {
    static final String LIST = """
            [
              {"kind": "Epika", "title": "Gabinet Starożytności", "author": "Honoré de Balzac",
               "href": "https://wolnelektury.pl/api/books/balzac-gabinet-starozytnosci/", "genre": "Powieść"},
              {"kind": "Liryka", "title": "Do M***", "author": "Adam Mickiewicz",
               "href": "https://wolnelektury.pl/api/books/do-m/", "genre": "Wiersz"},
              {"kind": "Epika", "title": "Lalka", "author": "Bolesław Prus",
               "href": "https://wolnelektury.pl/api/books/lalka/", "genre": "Powieść"}
            ]
            """;

    @Test
    void proseIsTakenBySlugAndPoemsAreNot() {
        var texts = WolneLekturyCatalogSource.parse(LIST, new ObjectMapper());
        assertThat(texts)
                .containsExactly(
                        new LibraryCatalogSource.LibraryText(
                                "WOLNE_LEKTURY",
                                "balzac-gabinet-starozytnosci",
                                "Gabinet Starożytności",
                                "Honoré de Balzac",
                                "pl"),
                        new LibraryCatalogSource.LibraryText("WOLNE_LEKTURY", "lalka", "Lalka", "Bolesław Prus", "pl"));
    }
}
