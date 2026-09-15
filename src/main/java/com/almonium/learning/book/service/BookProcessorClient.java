package com.almonium.learning.book.service;

import com.almonium.analyzer.client.exception.ApiIntegrationException;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.dto.response.BooksSpendLine;
import com.almonium.learning.book.dto.response.ProcessorIngestStatus;
import com.almonium.learning.book.dto.response.ProcessorTranslationEstimate;
import com.almonium.learning.book.dto.response.ProcessorTranslationStatus;
import com.almonium.learning.book.dto.response.StoredFile;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
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
import org.springframework.web.client.HttpClientErrorException;
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

    /** Forgets the processor's copy of a private import. A copy it no longer has is already as deleted as it gets. */
    public void deletePrivateImport(UUID importId, UUID ownerId) {
        String url = UriComponentsBuilder.fromUriString(processorUrl + "/internal/imports/{id}/")
                .queryParam("owner_id", ownerId)
                .buildAndExpand(importId)
                .toUriString();
        try {
            restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(internalHeaders()), Void.class);
        } catch (HttpClientErrorException.NotFound alreadyGone) {
            // nothing to do
        } catch (RestClientException exception) {
            throw new ApiIntegrationException("Book processor request failed", exception);
        }
    }

    /** The owner's upload, streamed through for a reviewer; the processor keeps the only copy. */
    public StoredFile downloadPrivateImportSource(UUID importId, UUID ownerId) {
        String url = UriComponentsBuilder.fromUriString(processorUrl + "/internal/imports/{id}/source/")
                .queryParam("owner_id", ownerId)
                .buildAndExpand(importId)
                .toUriString();
        ResponseEntity<byte[]> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(internalHeaders()), byte[].class);
        } catch (RestClientException exception) {
            throw new ApiIntegrationException("Book processor request failed", exception);
        }
        String filename = response.getHeaders().getContentDisposition().getFilename();
        MediaType type = response.getHeaders().getContentType();
        return new StoredFile(
                response.getBody() == null ? new byte[0] : response.getBody(),
                filename == null || filename.isBlank() ? "book" : filename,
                type == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : type.toString());
    }

    /** What a translation would cost before it is approved; the processor prices its own models. */
    public ProcessorTranslationEstimate estimateTranslation(
            String editionSlug, Language language, String tier, String mode) {
        String url = UriComponentsBuilder.fromUriString(processorUrl + "/internal/translations/estimate/")
                .queryParam("edition_slug", editionSlug)
                .queryParam("target_language", processorCode(language))
                .queryParam("tier", tier)
                .queryParam("mode", mode)
                .toUriString();
        JsonNode body = getJson(url, "estimate");
        return new ProcessorTranslationEstimate(
                uuid(body.path("edition_id")),
                body.path("edition_slug").asString(""),
                body.path("chapters").asInt(0),
                body.path("blocks").asInt(0),
                new BigDecimal(body.path("estimated_cost_usd").asString("0")));
    }

    /** Posts one translation job. Idempotent on {@code jobId}: the processor returns the edition it already made. */
    public ProcessorTranslationStatus startTranslation(
            UUID jobId, String editionSlug, Language language, String tier, String mode) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("job_id", jobId.toString());
        body.put("edition_slug", editionSlug);
        body.put("target_language", processorCode(language));
        body.put("tier", tier);
        body.put("mode", mode);
        body.put("register", "contemporary neutral");
        body.put("auto_publish", true);
        HttpHeaders headers = internalHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JsonNode response;
        try {
            response = restTemplate
                    .exchange(
                            processorUrl + "/internal/translations/",
                            HttpMethod.POST,
                            new HttpEntity<>(body, headers),
                            JsonNode.class)
                    .getBody();
        } catch (RestClientException exception) {
            throw new ApiIntegrationException("Book processor request failed", exception);
        }
        if (response == null) {
            throw new ApiIntegrationException("Book processor answered the translation request with nothing");
        }
        return translationStatus(response);
    }

    public ProcessorTranslationStatus translationStatus(UUID editionId) {
        return translationStatus(getJson(processorUrl + "/internal/translations/" + editionId + "/", "translation"));
    }

    /** Best effort on the processor's side too: a batch already running may not stop, but nothing new starts. */
    public ProcessorTranslationStatus cancelTranslation(UUID editionId) {
        JsonNode response;
        try {
            response = restTemplate
                    .exchange(
                            processorUrl + "/internal/translations/" + editionId + "/cancel/",
                            HttpMethod.POST,
                            new HttpEntity<>(internalHeaders()),
                            JsonNode.class)
                    .getBody();
        } catch (RestClientException exception) {
            throw new ApiIntegrationException("Book processor request failed", exception);
        }
        if (response == null) {
            throw new ApiIntegrationException("Book processor answered the cancellation with nothing");
        }
        return translationStatus(response);
    }

    /** Posts one library ingest seeded from a private import. Idempotent on {@code suggestionId}. */
    public ProcessorIngestStatus startLibraryIngest(
            UUID suggestionId,
            UUID importId,
            UUID ownerId,
            String title,
            String author,
            String description,
            Language language,
            Integer publicationYear) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("suggestion_id", suggestionId.toString());
        body.put("import_id", importId.toString());
        body.put("owner_id", ownerId.toString());
        body.put("title", title);
        body.put("author", author);
        body.put("description", description == null ? "" : description);
        body.put("language", processorCode(language));
        body.put("publication_year", publicationYear);
        HttpHeaders headers = internalHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JsonNode response;
        try {
            response = restTemplate
                    .exchange(
                            processorUrl + "/internal/library-ingests/",
                            HttpMethod.POST,
                            new HttpEntity<>(body, headers),
                            JsonNode.class)
                    .getBody();
        } catch (RestClientException exception) {
            throw new ApiIntegrationException("Book processor request failed", exception);
        }
        if (response == null) {
            throw new ApiIntegrationException("Book processor answered the ingest request with nothing");
        }
        return ingestStatus(response);
    }

    public ProcessorIngestStatus libraryIngestStatus(UUID editionId) {
        return ingestStatus(getJson(processorUrl + "/internal/library-ingests/" + editionId + "/", "ingest"));
    }

    private JsonNode getJson(String url, String what) {
        JsonNode body;
        try {
            body = restTemplate
                    .exchange(url, HttpMethod.GET, new HttpEntity<>(internalHeaders()), JsonNode.class)
                    .getBody();
        } catch (RestClientException exception) {
            throw new ApiIntegrationException("Book processor request failed", exception);
        }
        if (body == null) {
            throw new ApiIntegrationException("Book processor answered the " + what + " request with nothing");
        }
        return body;
    }

    private static ProcessorTranslationStatus translationStatus(JsonNode body) {
        JsonNode progress = body.path("progress");
        return new ProcessorTranslationStatus(
                uuid(body.path("edition_id")),
                body.path("edition_slug").asString(""),
                body.path("status").asString(""),
                body.path("phase").asString(""),
                progress.isObject() ? progress.path("completed").asInt(0) : null,
                progress.isObject() ? progress.path("total").asInt(0) : null,
                new BigDecimal(body.path("estimated_cost_usd").asString("0")),
                body.path("error").asString(""),
                instant(body.path("started_at")),
                instant(body.path("published_at")),
                uuid(body.path("published_book_id")));
    }

    private static ProcessorIngestStatus ingestStatus(JsonNode body) {
        return new ProcessorIngestStatus(
                uuid(body.path("edition_id")),
                body.path("slug").asString(""),
                body.path("status").asString(""),
                body.path("phase").asString(""),
                body.path("progress").asInt(0),
                body.path("error").asString(""),
                instant(body.path("published_at")),
                uuid(body.path("published_book_id")));
    }

    private static UUID uuid(JsonNode node) {
        String text = node.asString("");
        return text.isBlank() ? null : UUID.fromString(text);
    }

    private static Instant instant(JsonNode node) {
        String text = node.asString("");
        return text.isBlank() ? null : OffsetDateTime.parse(text).toInstant();
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
