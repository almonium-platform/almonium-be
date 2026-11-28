package com.almonium.learning.cover.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.learning.cover.service.CoverSearch.Kind;
import com.almonium.learning.cover.service.CoverSearch.Promise;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class CoverSearchTest {
    @Test
    void illustrationCategoriesAreOpenedFilmsRecordingsAndMonumentsNever() {
        assertThat(CoverSearch.classify("Illustrations of Through the Looking-Glass and What Alice Found There"))
                .isEqualTo(Kind.ILLUSTRATION);
        assertThat(CoverSearch.classify("Don Quixote in art")).isEqualTo(Kind.ILLUSTRATION);
        assertThat(CoverSearch.classify("Northanger Abbey (C. E. Brock)")).isEqualTo(Kind.OTHER);
        assertThat(CoverSearch.classify("Black Beauty (1877, Jarrold and Sons)"))
                .isEqualTo(Kind.EDITION);
        for (String skipped : List.of(
                "Alice Through the Looking Glass (2016 film)",
                "LibriVox - Wind in the Willows",
                "Toad of Toad Hall (play)",
                "Songs from Alice in wonderland and Through the looking-glass",
                "E H Shepard lived here (blue plaque), Lodsworth",
                "Route of Don Quixote",
                "Decorative letters from Don Quixote",
                "Don Quixote in the performing arts")) {
            assertThat(CoverSearch.classify(skipped)).as(skipped).isEqualTo(Kind.SKIP);
        }
    }

    @Test
    void aSubcategoryOfPicturesIsPicturesWhateverItIsCalled() {
        assertThat(CoverSearch.classify("John Tenniel", Kind.ILLUSTRATION)).isEqualTo(Kind.ILLUSTRATION);
        assertThat(CoverSearch.classify("John Tenniel", Kind.OTHER)).isEqualTo(Kind.OTHER);
        assertThat(CoverSearch.classify("Alice in Wonderland (1951 film)", Kind.ILLUSTRATION))
                .isEqualTo(Kind.SKIP);
    }

    @Test
    void aFileNameSpeaksForOrAgainstItself() {
        assertThat(CoverSearch.promise("File:Frontispiece to Frankenstein 1831.jpg"))
                .isEqualTo(Promise.LIKELY);
        assertThat(CoverSearch.promise("File:Thomson-PP11.jpg")).isEqualTo(Promise.UNKNOWN);
        for (String unlikely : List.of(
                "File:PrideAndPrejudiceTitlePage.jpg",
                "File:Moj Kary - pamietnik konia 1929 (page 12).jpg",
                "File:Jane Austen portrait.jpg",
                "File:Dracula 1931 film poster.jpg")) {
            assertThat(CoverSearch.promise(unlikely)).as(unlikely).isEqualTo(Promise.UNLIKELY);
        }
        assertThat(CoverSearch.raster("File:Austen - Emma.djvu")).isFalse();
    }

    @Test
    void aScannedVolumeIsSampledThroughItsLengthNotFromItsFrontMatter() {
        List<String> pages = IntStream.rangeClosed(1, 231)
                .mapToObj(page -> "File:Moj Kary %03d.jpg".formatted(page))
                .toList();

        List<String> sample = CoverSearch.sample(pages, 16);

        assertThat(sample).hasSize(16).startsWith("File:Moj Kary 001.jpg").endsWith("File:Moj Kary 231.jpg");
        assertThat(sample).contains("File:Moj Kary 108.jpg");
    }

    @Test
    void promisingNamesAreSampledBeforeTheRestAndTitlePagesLast() {
        List<String> files = List.of(
                "File:Title page.jpg", "File:Scan 1.jpg", "File:Illustration of the ball.jpg", "File:Scan 2.jpg");

        assertThat(CoverSearch.sample(files, 3))
                .containsExactly("File:Illustration of the ball.jpg", "File:Scan 1.jpg", "File:Scan 2.jpg");
        assertThat(CoverSearch.sample(files, 9)).endsWith("File:Title page.jpg");
    }

    @Test
    void candidatesAreDealtAcrossCategoriesSoOneLargeOneCannotCrowdOutTheRest() {
        record File(String name, String category) {}
        List<File> files = List.of(
                new File("Scan 1", "Complete works (1940)"),
                new File("Scan 2", "Complete works (1940)"),
                new File("Scan 3", "Complete works (1940)"),
                new File("Tenniel 1", "Tenniel's illustrations"),
                new File("Title page", "Tenniel's illustrations"),
                new File("Illustration 2", "Tenniel's illustrations"));

        List<File> dealt = CoverSearch.deal(files, File::category, File::name, 4);

        assertThat(dealt).extracting(File::name).containsExactly("Scan 1", "Illustration 2", "Scan 2", "Tenniel 1");
        assertThat(CoverSearch.deal(files, File::category, (Function<File, String>) File::name, 99))
                .hasSize(6);
    }
}
