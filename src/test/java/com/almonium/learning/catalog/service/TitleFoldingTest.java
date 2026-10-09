package com.almonium.learning.catalog.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TitleFoldingTest {
    @Test
    void foldsCaseAccentsApostrophesAndPunctuation() {
        assertThat(TitleFolding.fold("Bahnwärter Thiel")).isEqualTo("bahnwarter thiel");
        assertThat(TitleFolding.fold("Harry Potter and the Philosopher’s Stone"))
                .isEqualTo("harry potter and the philosophers stone");
        assertThat(TitleFolding.fold("Die Straße — ein Roman!")).isEqualTo("die strasse ein roman");
        assertThat(TitleFolding.fold("Ёлка")).isEqualTo(TitleFolding.fold("елка"));
        assertThat(TitleFolding.fold(null)).isEmpty();
    }

    @Test
    void theMainTitleDropsTheSubtitle() {
        assertThat(TitleFolding.mainTitle("Frankenstein; or, the modern prometheus"))
                .isEqualTo("Frankenstein");
        assertThat(TitleFolding.mainTitle("Lincoln's Gettysburg Address\nGiven November 19, 1863"))
                .isEqualTo("Lincoln's Gettysburg Address");
        assertThat(TitleFolding.mainTitle("Dracula")).isEqualTo("Dracula");
    }
}
