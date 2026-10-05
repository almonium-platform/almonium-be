package com.almonium.learning.catalog.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
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
 * Project Gutenberg's catalogue file: one row per text, with the authors' life dates in the author field ("Shelley,
 * Mary Wollstonecraft, 1797-1851"). Gutenberg only checks United States copyright, so a row here is a text we could
 * start from, not a verdict; the copyright engine still judges the authors' dates.
 */
@Slf4j
@Component
public class GutenbergCatalogSource {
    private static final Pattern DATES = Pattern.compile("(\\d{1,4})\\??\\s*(BCE)?\\s*-\\s*(\\d{1,4})\\??\\s*(BCE)?$");
    private static final Pattern ROLE = Pattern.compile("\\s*\\[[^\\]]*\\]\\s*$");

    private final RestClient restClient;
    private final String catalogueUrl;

    public GutenbergCatalogSource(
            @Value("${app.catalog.gutenberg-csv-url:https://www.gutenberg.org/cache/epub/feeds/pg_catalog.csv}")
                    String catalogueUrl) {
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory();
        factory.setReadTimeout(Duration.ofMinutes(5));
        this.restClient = RestClient.builder().requestFactory(factory).build();
        this.catalogueUrl = catalogueUrl;
    }

    /** One author of a text: the name as "First Last", and the life years when Gutenberg records them. */
    public record Author(String name, String surname, Integer birthYear, Integer deathYear) {}

    public record Text(int id, String title, String language, List<Author> authors) {}

    public List<Text> fetch() {
        List<Text> texts = restClient.get().uri(catalogueUrl).exchange((request, response) -> {
            if (response.getStatusCode().isError()) {
                throw new IllegalStateException("Gutenberg catalogue answered " + response.getStatusCode());
            }
            return parse(new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8)));
        });
        log.info("Gutenberg catalogue: {} texts", texts.size());
        return texts;
    }

    static List<Text> parse(Reader reader) throws IOException {
        List<Text> texts = new ArrayList<>();
        List<List<String>> rows = Csv.read(reader);
        for (List<String> row : rows.subList(Math.min(1, rows.size()), rows.size())) {
            if (row.size() < 6 || !"Text".equals(row.get(1))) {
                continue;
            }
            Integer id = SparqlTsv.integer(row.get(0));
            if (id == null || row.get(3).isBlank()) {
                continue;
            }
            String language = row.get(4).split(";")[0].trim();
            texts.add(new Text(id, row.get(3), language, authors(row.get(5))));
        }
        return texts;
    }

    /** The primary authors only: translators, editors and illustrators carry a bracketed role and are left out. */
    static List<Author> authors(String field) {
        List<Author> authors = new ArrayList<>();
        if (field == null || field.isBlank()) {
            return authors;
        }
        for (String entry : field.split(";")) {
            String trimmed = entry.trim();
            if (trimmed.isEmpty() || ROLE.matcher(trimmed).find()) {
                continue;
            }
            Integer born = null;
            Integer died = null;
            Matcher dates = DATES.matcher(trimmed);
            String name = trimmed;
            if (dates.find()) {
                born = signed(dates.group(1), dates.group(2));
                died = signed(dates.group(3), dates.group(4) != null ? dates.group(4) : dates.group(2));
                name = trimmed.substring(0, dates.start()).replaceAll("[,\\s]+$", "");
            }
            String[] parts = name.split(",");
            String surname = parts[0].trim();
            String first =
                    parts.length > 1 ? parts[1].replaceAll("\\(.*\\)", "").trim() : "";
            String display = first.isEmpty() ? surname : first + " " + surname;
            if (surname.equalsIgnoreCase("Anonymous")
                    || surname.equalsIgnoreCase("Various")
                    || surname.equalsIgnoreCase("Unknown")) {
                continue;
            }
            authors.add(new Author(display, surname, born, died));
        }
        return authors;
    }

    private static Integer signed(String year, String era) {
        int value = Integer.parseInt(year);
        return era != null ? -value : value;
    }
}
