package com.almonium.learning.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.config.properties.AppProperties;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.rights.service.WorkRightsService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.ResourceAccessException;

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
        service = new BookSitemapService(bookRepository, content, properties, mock(WorkRightsService.class));
    }

    @Test
    void listsPublicPagesBooksAndEveryChapterByItsProcessorSequence() {
        when(bookRepository.findAll()).thenReturn(List.of(book("the-hobbit-en"), book("alice-en")));
        when(content.chapterSequences())
                .thenReturn(
                        Map.of("the-hobbit-en", List.of(12, 11), "alice-en", List.of(1), "unlisted-en", List.of(1)));

        String xml = service.sitemap();

        assertThat(xml)
                .startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .contains("<loc>https://almonium.com/</loc>")
                .contains("<loc>https://almonium.com/pricing</loc>")
                .contains("<loc>https://almonium.com/books/the-hobbit-en/11</loc>")
                .contains("<loc>https://almonium.com/books/the-hobbit-en/12</loc>")
                .contains("<loc>https://almonium.com/books/alice-en/1</loc>")
                .doesNotContain("/books/the-hobbit-en/1<")
                .doesNotContain("unlisted-en")
                .endsWith("</urlset>\n");
        assertThat(xml.indexOf("/books/alice-en<")).isLessThan(xml.indexOf("/books/the-hobbit-en<"));
        assertThat(xml.indexOf("/the-hobbit-en/11<")).isLessThan(xml.indexOf("/the-hobbit-en/12<"));
    }

    @Test
    void servesTheBuiltDocumentWithoutAskingTheProcessorAgainUntilARefresh() {
        when(bookRepository.findAll()).thenReturn(List.of(book("alice-en")));
        when(content.chapterSequences())
                .thenReturn(Map.of("alice-en", List.of(1)))
                .thenReturn(Map.of("alice-en", List.of(1, 2)));

        assertThat(service.sitemap()).doesNotContain("/alice-en/2<");
        assertThat(service.sitemap()).doesNotContain("/alice-en/2<");
        verify(content, times(1)).chapterSequences();

        service.refresh();
        assertThat(service.sitemap()).contains("/alice-en/2<");
        verify(content, times(2)).chapterSequences();
    }

    @Test
    void keepsTheLastGoodDocumentWhenTheProcessorFailsOnARefresh() {
        when(bookRepository.findAll()).thenReturn(List.of(book("alice-en")));
        when(content.chapterSequences())
                .thenReturn(Map.of("alice-en", List.of(1)))
                .thenThrow(new ResourceAccessException("Read timed out"));

        service.refresh();
        service.refresh();

        assertThat(service.sitemap()).contains("<loc>https://almonium.com/books/alice-en/1</loc>");
    }

    @Test
    void listsBookPagesOnlyUntilTheProcessorFirstAnswers() {
        when(bookRepository.findAll()).thenReturn(List.of(book("new-book-de")));
        when(content.chapterSequences())
                .thenThrow(new ResourceAccessException("Connection refused"))
                .thenReturn(Map.of("new-book-de", List.of(3)));

        assertThat(service.sitemap())
                .contains("<loc>https://almonium.com/books/new-book-de</loc>")
                .doesNotContain("/books/new-book-de/");

        service.refresh();
        assertThat(service.sitemap()).contains("<loc>https://almonium.com/books/new-book-de/3</loc>");
    }

    private static Book book(String slug) {
        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setEditionSlug(slug);
        return book;
    }
}
