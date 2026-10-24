package com.almonium.learning.catalog.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SourceOrderTest {
    SourceOrder order =
            new SourceOrder("en=STANDARD_EBOOKS,GUTENBERG;pl=WOLNE_LEKTURY,GUTENBERG;*=GUTENBERG,WIKISOURCE");

    @Test
    void aLanguagesListComesFirstThenTheGeneralOneThenTheRest() {
        assertThat(order.rank("en", "STANDARD_EBOOKS")).isEqualTo(0);
        assertThat(order.rank("en", "GUTENBERG")).isEqualTo(1);
        // Wikisource is not on the English list: after it, in the general order
        assertThat(order.rank("en", "WIKISOURCE")).isEqualTo(2 + 1);
        // a library on no list at all comes last
        assertThat(order.rank("en", "BEQ")).isEqualTo(2 + 2);
        assertThat(order.rank("pl", "WOLNE_LEKTURY")).isEqualTo(0);
        // a language with no list of its own takes the general order
        assertThat(order.rank("de", "GUTENBERG")).isEqualTo(0);
        assertThat(order.rank("de", "WIKISOURCE")).isEqualTo(1);
        assertThat(order.rank(null, "gutenberg")).isEqualTo(0);
    }
}
