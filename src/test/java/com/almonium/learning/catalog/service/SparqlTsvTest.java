package com.almonium.learning.catalog.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SparqlTsvTest {
    @Test
    void readsEntitiesLiteralsNumbersAndEmptyCells() {
        var cells =
                SparqlTsv.raw("<http://www.wikidata.org/entity/Q25338>\t151\t\"fr\"\t\t\"Le \\\"Petit\\\" Prince\"@fr");

        assertThat(SparqlTsv.value(cells.get(0))).isEqualTo("Q25338");
        assertThat(SparqlTsv.integer(SparqlTsv.value(cells.get(1)))).isEqualTo(151);
        assertThat(SparqlTsv.value(cells.get(2))).isEqualTo("fr");
        assertThat(SparqlTsv.value(cells.get(3))).isEmpty();
        assertThat(SparqlTsv.value(cells.get(4))).isEqualTo("Le \"Petit\" Prince");
        assertThat(SparqlTsv.language(cells.get(4))).isEqualTo("fr");
        assertThat(SparqlTsv.integer("-800")).isEqualTo(-800);
        assertThat(SparqlTsv.integer("not a number")).isNull();
    }
}
