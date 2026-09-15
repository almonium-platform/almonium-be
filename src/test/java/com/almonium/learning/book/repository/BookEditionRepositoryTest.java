package com.almonium.learning.book.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.config.PostgresContainer;
import com.almonium.learning.book.model.entity.Book;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

@DataJpaTest
@ImportTestcontainers(PostgresContainer.class)
class BookEditionRepositoryTest {
    @Autowired
    BookRepository books;

    @Test
    void discoversSiblingsAndSameLanguageLevelsButNotWithdrawnEditions() {
        Book original = edition("novel-original", Language.EN, CEFR.C1, null);
        Book adaptation = edition("novel-b2", Language.EN, CEFR.B2, original);
        Book ukrainian = edition("novel-uk", Language.UK, CEFR.C1, original);
        assertThat(books.findAvailableLanguagesForBook(adaptation.getId()))
                .extracting("editionSlug")
                .containsExactlyInAnyOrder("novel-original", "novel-b2", "novel-uk");
        var variants = books.findAvailableLanguagesForBook(adaptation.getId());
        assertThat(variants.stream()
                        .filter(v -> v.getEditionSlug().equals("novel-b2"))
                        .findFirst()
                        .orElseThrow()
                        .getCefrLevel())
                .isEqualTo("B2");
        ukrainian.setWithdrawnAt(Instant.now());
        books.saveAndFlush(ukrainian);
        assertThat(books.findAvailableLanguagesForBook(adaptation.getId())).hasSize(2);
    }

    private Book edition(String slug, Language language, CEFR level, Book source) {
        Book book = new Book();
        book.setEditionSlug(slug);
        book.setWorkSlug("novel");
        book.setSourceHash("a".repeat(64));
        book.setTitle("Novel");
        book.setAuthor("Author");
        book.setPublicationYear(1818);
        book.setLanguage(language);
        book.setOriginalLanguage(Language.EN);
        book.setCefrLevel(level);
        book.setEditionType(source == null ? "original" : "adaptation");
        book.setOriginalBook(source);
        return books.saveAndFlush(book);
    }
}
