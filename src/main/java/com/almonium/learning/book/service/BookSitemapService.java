package com.almonium.learning.book.service;

import com.almonium.config.properties.AppProperties;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.rights.model.entity.WorkRightsBlock;
import com.almonium.learning.rights.service.WorkRightsService;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

/**
 * The sitemap a search engine reads (docs/SEO.md in the frontend): the public pages, every catalogue book and each
 * of its chapters, on the app's own domain. Chapter URLs carry the processor's sequence, which need not run 1..n,
 * so the sequences of every published edition are asked of the processor in one call.
 *
 * <p>A crawler is served the last document built, never one built while it waits: {@link #refresh()} rebuilds it on
 * a schedule (SitemapRefreshTask), and only the first request after a start without one builds it inline. A
 * rebuild the processor cannot answer keeps the last good document; with none yet, books are listed without their
 * chapters and the task tries again within minutes. Withdrawn books never appear, since the entity hides them from every query.
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
    WorkRightsService workRightsService;

    /** The last document built with the processor's chapters, or one without them if none has been yet. */
    AtomicReference<Built> latest = new AtomicReference<>();

    public String sitemap() {
        Built built = latest.get();
        return built != null ? built.xml() : refresh();
    }

    /** Whether the document served lists the processor's chapters; false before the first build too. */
    public boolean hasChapters() {
        Built built = latest.get();
        return built != null && built.withChapters();
    }

    /** Rebuilds the document; returns what is served afterwards. */
    public String refresh() {
        Map<String, List<Integer>> sequences;
        try {
            sequences = publishedBookContentService.chapterSequences();
        } catch (RuntimeException unavailable) {
            Built previous = latest.get();
            if (previous != null && previous.withChapters()) {
                log.warn("Sitemap keeps its last build; processor chapters unavailable: {}", unavailable.getMessage());
                return previous.xml();
            }
            log.warn("Sitemap lists books without chapters; processor unavailable: {}", unavailable.getMessage());
            String xml = build(Map.of());
            latest.set(new Built(xml, false));
            return xml;
        }
        String xml = build(sequences);
        latest.set(new Built(xml, true));
        return xml;
    }

    private String build(Map<String, List<Integer>> sequences) {
        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                .append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
        STATIC_PATHS.forEach(path -> appendUrl(xml, path));
        bookRepository.findAll().stream()
                // A work taken down everywhere is not advertised; one hidden in some countries still is.
                .filter(book ->
                        !workRightsService.blockedCountries(book.getWorkSlug()).contains(WorkRightsBlock.EVERYWHERE))
                .sorted(Comparator.comparing(Book::getEditionSlug))
                .forEach(book -> {
                    String bookPath = "/books/" + book.getEditionSlug();
                    appendUrl(xml, bookPath);
                    sequences.getOrDefault(book.getEditionSlug(), List.of()).stream()
                            .sorted()
                            .forEach(sequence -> appendUrl(xml, bookPath + "/" + sequence));
                });
        return xml.append("</urlset>\n").toString();
    }

    private void appendUrl(StringBuilder xml, String path) {
        String location = "/".equals(path) ? appProperties.getWebDomain() + "/" : appProperties.getWebDomain() + path;
        xml.append("  <url><loc>").append(HtmlUtils.htmlEscape(location)).append("</loc></url>\n");
    }

    private record Built(String xml, boolean withChapters) {}
}
