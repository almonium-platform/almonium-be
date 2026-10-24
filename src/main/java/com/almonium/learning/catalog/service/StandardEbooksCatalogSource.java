package com.almonium.learning.catalog.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Standard Ebooks (standardebooks.org): about fifteen hundred English books, each one chapter a file with a full
 * table of contents; ten measured on 2026-10-24 were sound without a finding. The machine-readable feed is for
 * patrons, so the catalogue reads the public listing, twelve books a page, until a page lists none. A text is
 * referenced by its path, {@code /ebooks/<author>/<title>}, from which its page and download addresses follow.
 */
@Slf4j
@Component
public class StandardEbooksCatalogSource implements LibraryCatalogSource {
    static final Pattern BOOK =
            Pattern.compile("<li typeof=\"schema:Book\" about=\"(/ebooks/[^\"]+)\">(.*?)</li>", Pattern.DOTALL);
    static final Pattern TITLE = Pattern.compile("<span property=\"schema:name\">([^<]*)</span>");
    static final Pattern AUTHOR = Pattern.compile(
            "<p class=\"author\"[^>]*>.*?<span property=\"schema:name\">([^<]*)</span>", Pattern.DOTALL);
    private static final int MAX_PAGES = 400;

    private final RestClient restClient;
    private final String listingUrl;

    public StandardEbooksCatalogSource(
            @Value("${app.catalog.standard-ebooks-url:https://standardebooks.org/ebooks}") String listingUrl) {
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory();
        factory.setReadTimeout(Duration.ofMinutes(2));
        this.restClient = RestClient.builder().requestFactory(factory).build();
        this.listingUrl = listingUrl;
    }

    @Override
    public String kind() {
        return CatalogRecord.STANDARD_EBOOKS;
    }

    @Override
    public List<LibraryText> fetch() {
        List<LibraryText> texts = new ArrayList<>();
        for (int page = 1; page <= MAX_PAGES; page++) {
            String html = restClient
                    .get()
                    .uri(listingUrl + "?page=" + page)
                    .retrieve()
                    .body(String.class);
            List<LibraryText> found = parse(html == null ? "" : html);
            if (found.isEmpty()) {
                break;
            }
            texts.addAll(found);
        }
        log.info("Standard Ebooks: {} texts", texts.size());
        return texts;
    }

    static List<LibraryText> parse(String html) {
        List<LibraryText> texts = new ArrayList<>();
        Matcher book = BOOK.matcher(html);
        while (book.find()) {
            String path = book.group(1);
            String entry = book.group(2);
            Matcher title = TITLE.matcher(entry);
            Matcher author = AUTHOR.matcher(entry);
            if (!title.find()) {
                continue;
            }
            texts.add(new LibraryText(
                    CatalogRecord.STANDARD_EBOOKS,
                    path,
                    unescape(title.group(1)),
                    author.find() ? unescape(author.group(1)) : "",
                    "en"));
        }
        return texts;
    }

    static String unescape(String text) {
        return text.replace("&amp;", "&")
                .replace("&#39;", "'")
                .replace("&quot;", "\"")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .trim();
    }
}
