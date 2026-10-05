package com.almonium.learning.catalog.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.learning.catalog.service.GutenbergCatalogSource.Author;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

class GutenbergCatalogSourceTest {
    @Test
    void parsesTextsWithQuotedMultilineTitlesAndSkipsAudio() throws Exception {
        String csv = """
                Text#,Type,Issued,Title,Language,Authors,Subjects,LoCC,Bookshelves
                84,Text,1993-10-01,"Frankenstein; or, the modern prometheus",en,"Shelley, Mary Wollstonecraft, 1797-1851",Horror,PR,Gothic
                4,Text,1973-11-01,"Lincoln's Gettysburg Address
                Given November 19, 1863",en,"Lincoln, Abraham, 1809-1865",,E456,
                9999,Sound,2005-01-01,An audio book,en,"Reader, A.",,,
                """;

        var texts = GutenbergCatalogSource.parse(new StringReader(csv));

        assertThat(texts).hasSize(2);
        assertThat(texts.getFirst().id()).isEqualTo(84);
        assertThat(texts.getFirst().authors())
                .containsExactly(new Author("Mary Wollstonecraft Shelley", "Shelley", 1797, 1851));
        assertThat(texts.get(1).title()).startsWith("Lincoln's Gettysburg Address\n");
    }

    @Test
    void keepsPrimaryAuthorsOnlyAndReadsTheirYears() {
        assertThat(GutenbergCatalogSource.authors(
                        "Tolstoy, Leo, graf, 1828-1910; Maude, Aylmer, 1858-1938 [Translator]"))
                .containsExactly(new Author("Leo Tolstoy", "Tolstoy", 1828, 1910));
        assertThat(GutenbergCatalogSource.authors("Homer, 751? BCE-651? BCE"))
                .containsExactly(new Author("Homer", "Homer", -751, -651));
        assertThat(GutenbergCatalogSource.authors("Kennedy, John F. (John Fitzgerald), 1917-1963"))
                .containsExactly(new Author("John F. Kennedy", "Kennedy", 1917, 1963));
        assertThat(GutenbergCatalogSource.authors("Anonymous")).isEmpty();
        assertThat(GutenbergCatalogSource.authors("Doe, Jane"))
                .containsExactly(new Author("Jane Doe", "Doe", null, null));
    }
}
