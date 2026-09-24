package com.almonium.learning.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.almonium.config.properties.AppProperties;
import com.almonium.learning.book.dto.response.BookChapter;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** The public pages, each book and every chapter it has, on the app domain. */
@ExtendWith(MockitoExtension.class)
class BookSitemapServiceTest {
    @Mock
    BookRepository bookRepository;

    @Mock
    PublishedBookContentService content;

    BookSitemapService service;

    @BeforeEach
    void setUp() {
        AppProperties properties = new AppProperties();
        properties.setWebDomain("https://almonium.com");
        service = new BookSitemapService(bookRepository, content, properties);
    }

    @Test
    void listsPublicPagesBooksAndEveryChapterByItsProcessorSequence() {
        Book hobbit = book("the-hobbit-en");
        Book alice = book("alice-en");
        when(bookRepository.findAll()).thenReturn(List.of(hobbit, alice));
        when(content.chaptersFor(hobbit)).thenReturn(List.of(chapter(12), chapter(11)));
        when(content.chaptersFor(alice)).thenReturn(List.of(chapter(1)));

        String xml = service.sitemap();

        assertThat(xml)
                .startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .contains("<loc>https://almonium.com/</loc>")
                .contains("<loc>https://almonium.com/pricing</loc>")
                .contains("<loc>https://almonium.com/books/the-hobbit-en/11</loc>")
                .contains("<loc>https://almonium.com/books/the-hobbit-en/12</loc>")
                .contains("<loc>https://almonium.com/books/alice-en/1</loc>")
                .doesNotContain("/books/the-hobbit-en/1<")
                .endsWith("</urlset>\n");
        assertThat(xml.indexOf("/books/alice-en<")).isLessThan(xml.indexOf("/books/the-hobbit-en<"));
        assertThat(xml.indexOf("/the-hobbit-en/11<")).isLessThan(xml.indexOf("/the-hobbit-en/12<"));
    }

    @Test
    void listsOnlyTheBookPageWhenTheProcessorCannotGiveItsChapters() {
        Book book = book("new-book-de");
        when(bookRepository.findAll()).thenReturn(List.of(book));
        when(content.chaptersFor(book))
                .thenThrow(new EntityNotFoundException("Published edition chapters are unavailable"));

        assertThat(service.sitemap())
                .contains("<loc>https://almonium.com/books/new-book-de</loc>")
                .doesNotContain("/books/new-book-de/");
    }

    private static Book book(String slug) {
        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setEditionSlug(slug);
        return book;
    }

    private static BookChapter chapter(int sequence) {
        return new BookChapter(UUID.randomUUID(), sequence, "Chapter " + sequence, "complete", null, List.of());
    }
}
