package com.almonium.learning.book.service;

import com.almonium.config.properties.AppProperties;
import com.almonium.learning.book.dto.response.BookChapter;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.repository.BookRepository;
import java.util.Comparator;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

/**
 * The sitemap a search engine reads (docs/SEO.md in the frontend): the public pages, every catalogue book and each
 * of its chapters, on the app's own domain. Chapter URLs carry the processor's sequence, which need not run 1..n,
 * so each book's chapters are asked of the processor; a book whose chapters it cannot give lists its page only.
 * Withdrawn books never appear, since the entity hides them from every query.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookSitemapService {
    /** The app's public pages that stand on their own; everything behind sign-in is left out. */
    static final List<String> STATIC_PATHS =
            List.of("/", "/read", "/discover", "/play", "/pricing", "/terms-of-use", "/privacy-policy");

    BookRepository bookRepository;
    PublishedBookContentService publishedBookContentService;
    AppProperties appProperties;

    public String sitemap() {
        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                .append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
        STATIC_PATHS.forEach(path -> appendUrl(xml, path));
        bookRepository.findAll().stream()
                .sorted(Comparator.comparing(Book::getEditionSlug))
                .forEach(book -> {
                    String bookPath = "/books/" + book.getEditionSlug();
                    appendUrl(xml, bookPath);
                    chapterSequences(book).forEach(sequence -> appendUrl(xml, bookPath + "/" + sequence));
                });
        return xml.append("</urlset>\n").toString();
    }

    private List<Integer> chapterSequences(Book book) {
        try {
            return publishedBookContentService.chaptersFor(book).stream()
                    .map(BookChapter::sequence)
                    .sorted()
                    .toList();
        } catch (RuntimeException unavailable) {
            log.warn("Sitemap lists {} without chapters: {}", book.getEditionSlug(), unavailable.getMessage());
            return List.of();
        }
    }

    private void appendUrl(StringBuilder xml, String path) {
        String location = "/".equals(path) ? appProperties.getWebDomain() + "/" : appProperties.getWebDomain() + path;
        xml.append("  <url><loc>").append(HtmlUtils.htmlEscape(location)).append("</loc></url>\n");
    }
}
