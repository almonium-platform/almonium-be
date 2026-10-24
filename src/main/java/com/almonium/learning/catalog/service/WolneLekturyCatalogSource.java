package com.almonium.learning.catalog.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

/**
 * Wolne Lektury (wolnelektury.pl, Fundacja Wolne Lektury): the Polish school canon and much more, 7,665 titles with
 * an open API; eleven of twelve measured on 2026-10-24 were sound once its own pages were trimmed. Prose only
 * ("Epika"): the library also holds thousands of single poems, which are not books to read through. A text is
 * referenced by its slug, the last part of its address, from which its page and EPUB addresses follow. Every text
 * is in Polish; one whose work is not is a translation, by translators the list does not name.
 */
@Slf4j
@Component
public class WolneLekturyCatalogSource implements LibraryCatalogSource {
    private static final String PROSE = "Epika";

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String listUrl;

    public WolneLekturyCatalogSource(
            ObjectMapper objectMapper,
            @Value("${app.catalog.wolne-lektury-url:https://wolnelektury.pl/api/books/?format=json}") String listUrl) {
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory();
        factory.setReadTimeout(Duration.ofMinutes(2));
        this.restClient = RestClient.builder().requestFactory(factory).build();
        this.objectMapper = objectMapper;
        this.listUrl = listUrl;
    }

    @Override
    public String kind() {
        return CatalogRecord.WOLNE_LEKTURY;
    }

    @Override
    public List<LibraryText> fetch() {
        String json = restClient.get().uri(listUrl).retrieve().body(String.class);
        List<LibraryText> texts = parse(json == null ? "[]" : json, objectMapper);
        log.info("Wolne Lektury: {} prose texts", texts.size());
        return texts;
    }

    static List<LibraryText> parse(String json, ObjectMapper objectMapper) {
        List<Map<String, Object>> rows = objectMapper.readValue(json, new TypeReference<>() {});
        List<LibraryText> texts = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            if (!PROSE.equals(row.get("kind"))) {
                continue;
            }
            String href = String.valueOf(row.getOrDefault("href", ""));
            String slug = slug(href);
            String title = String.valueOf(row.getOrDefault("title", "")).trim();
            if (slug.isEmpty() || title.isEmpty()) {
                continue;
            }
            texts.add(new LibraryText(
                    CatalogRecord.WOLNE_LEKTURY,
                    slug,
                    title,
                    String.valueOf(row.getOrDefault("author", "")).trim(),
                    "pl"));
        }
        return texts;
    }

    /** "balzac-gabinet-starozytnosci" from ".../api/books/balzac-gabinet-starozytnosci/". */
    static String slug(String href) {
        String trimmed = href.endsWith("/") ? href.substring(0, href.length() - 1) : href;
        int cut = trimmed.lastIndexOf('/');
        return cut < 0 ? trimmed : trimmed.substring(cut + 1);
    }
}
