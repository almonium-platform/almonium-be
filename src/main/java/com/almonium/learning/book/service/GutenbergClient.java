package com.almonium.learning.book.service;

import com.almonium.analyzer.translator.model.enums.Language;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * The public-domain index the ask sheet checks a title against (G19): Project Gutenberg, through the Gutendex API.
 * A match is a strong sign the work is public domain; no match is not a sign of anything, so the caller says "we
 * could not confirm" rather than "no". The index being down is the same as no match.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GutenbergClient {
    private final RestTemplate restTemplate;

    @Value("${app.books.gutenberg-url:https://gutendex.com}")
    private String gutenbergUrl;

    /** The best match for a title (and author, when given) in the language the reader would read it in. */
    public Optional<GutenbergMatch> find(String title, String author, Language language) {
        String search = author == null || author.isBlank() ? title : title + " " + author;
        if (search.isBlank()) {
            return Optional.empty();
        }
        String url = UriComponentsBuilder.fromUriString(gutenbergUrl + "/books")
                .queryParam("search", search.trim())
                .queryParam("languages", language.name().toLowerCase(Locale.ROOT))
                .build()
                .toUriString();
        try {
            SearchResponse response = restTemplate.getForObject(url, SearchResponse.class);
            if (response == null || response.results() == null) {
                return Optional.empty();
            }
            return response.results().stream()
                    .filter(result -> result.id() != null && result.title() != null)
                    .findFirst()
                    .map(result -> new GutenbergMatch(
                            result.id(),
                            result.title(),
                            result.authors() == null || result.authors().isEmpty()
                                    ? ""
                                    : result.authors().getFirst().name(),
                            result.authors() == null || result.authors().isEmpty()
                                    ? null
                                    : result.authors().getFirst().deathYear()));
        } catch (RestClientException exception) {
            log.warn("Gutenberg lookup failed for '{}': {}", search, exception.getMessage());
            return Optional.empty();
        }
    }

    /** A Gutenberg book: its catalogue id, the title and author as the catalogue spells them. */
    public record GutenbergMatch(int id, String title, String author, Integer authorDeathYear) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record SearchResponse(List<Result> results) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Result(Integer id, String title, List<Author> authors) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Author(
            String name,

            @com.fasterxml.jackson.annotation.JsonProperty("death_year")
            Integer deathYear) {}
}
