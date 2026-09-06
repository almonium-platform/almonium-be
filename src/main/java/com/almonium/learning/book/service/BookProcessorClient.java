package com.almonium.learning.book.service;

import com.almonium.analyzer.client.exception.ApiIntegrationException;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.dto.response.BooksSpendLine;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.JsonNode;

@Service
@RequiredArgsConstructor
public class BookProcessorClient {
    private final RestTemplate restTemplate;

    @Value("${app.books.processor-url}")
    private String processorUrl;

    @Value("${app.books.publisher-secret}")
    private String sharedSecret;

    public void createPrivateImport(
            UUID importId,
            UUID ownerId,
            String ownerLabel,
            MultipartFile source,
            String title,
            String author,
            String description,
            Language language,
            Integer publicationYear) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("import_id", importId.toString());
        body.add("owner_id", ownerId.toString());
        body.add("owner_label", ownerLabel == null ? "" : ownerLabel);
        // Whatever the owner left blank is detected from the file after ingestion.
        if (title != null && !title.isBlank()) body.add("title", title);
        if (author != null && !author.isBlank()) body.add("author", author);
        body.add("description", description == null ? "" : description);
        if (language != null) body.add("language", processorCode(language));
        if (publicationYear != null) body.add("publication_year", publicationYear.toString());
        try {
            body.add("source_file", new NamedByteArrayResource(source.getBytes(), source.getOriginalFilename()));
        } catch (java.io.IOException exception) {
            throw new IllegalArgumentException("Could not read uploaded book", exception);
        }

        HttpHeaders headers = internalHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        try {
            restTemplate.exchange(
                    processorUrl + "/internal/imports/",
                    HttpMethod.POST,
                    new HttpEntity<>(body, headers),
                    JsonNode.class);
        } catch (RestClientException exception) {
            throw new ApiIntegrationException("Book processor request failed", exception);
        }
    }

    /** Replaces the processor's copy of the owner's confirmed details. */
    public void updatePrivateImportMetadata(
            UUID importId,
            UUID ownerId,
            String title,
            String author,
            String description,
            Language language,
            Integer publicationYear) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("title", title);
        body.put("author", author);
        body.put("description", description == null ? "" : description);
        body.put("language", processorCode(language));
        body.put("publication_year", publicationYear);
        String url = UriComponentsBuilder.fromUriString(processorUrl + "/internal/imports/{id}/metadata/")
                .queryParam("owner_id", ownerId)
                .buildAndExpand(importId)
                .toUriString();
        HttpHeaders headers = internalHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(body, headers), JsonNode.class);
        } catch (RestClientException exception) {
            throw new ApiIntegrationException("Book processor request failed", exception);
        }
    }

    /** The processor speaks lower-case ISO 639-1 codes. */
    static String processorCode(Language language) {
        return language.name().toLowerCase(java.util.Locale.ROOT);
    }

    public JsonNode[] privateBlocks(UUID importId, UUID ownerId) {
        String url = UriComponentsBuilder.fromUriString(processorUrl + "/internal/imports/{id}/blocks/")
                .queryParam("owner_id", ownerId)
                .buildAndExpand(importId)
                .toUriString();
        ResponseEntity<JsonNode[]> response =
                restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(internalHeaders()), JsonNode[].class);
        return response.getBody();
    }

    /** The processor's AI run ledger summed per purpose and model; the processor prices its own runs. */
    public List<BooksSpendLine> aiSpend(Instant since, Instant until) {
        String url = UriComponentsBuilder.fromUriString(processorUrl + "/internal/ai-spend/")
                .queryParam("since", since.toString())
                .queryParam("until", until.toString())
                .toUriString();
        JsonNode body;
        try {
            body = restTemplate
                    .exchange(url, HttpMethod.GET, new HttpEntity<>(internalHeaders()), JsonNode.class)
                    .getBody();
        } catch (RestClientException exception) {
            throw new ApiIntegrationException("Book processor request failed", exception);
        }
        if (body == null) {
            throw new ApiIntegrationException("Book processor answered the spend request with nothing");
        }
        List<BooksSpendLine> lines = new ArrayList<>();
        body.path("lines")
                .forEach(line -> lines.add(new BooksSpendLine(
                        line.path("purpose").asString(""),
                        line.path("model").asString(""),
                        line.path("runs").asLong(0),
                        line.path("input_tokens").asLong(0),
                        line.path("cached_input_tokens").asLong(0),
                        line.path("output_tokens").asLong(0),
                        line.path("reasoning_tokens").asLong(0),
                        new BigDecimal(line.path("estimated_cost_usd").asString("0")))));
        return lines;
    }

    private HttpHeaders internalHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Almonium-Books-Token", sharedSecret);
        return headers;
    }

    private static final class NamedByteArrayResource extends ByteArrayResource {
        private final String filename;

        private NamedByteArrayResource(byte[] bytes, String filename) {
            super(bytes);
            this.filename = filename == null ? "book.epub" : filename;
        }

        @Override
        public String getFilename() {
            return filename;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (!(object instanceof NamedByteArrayResource other)) return false;
            return super.equals(object) && Objects.equals(filename, other.filename);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), filename);
        }
    }
}
