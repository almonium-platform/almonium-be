package com.almonium.learning.book.service;

import com.almonium.analyzer.translator.model.enums.Language;
import com.fasterxml.jackson.databind.JsonNode;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

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
            MultipartFile source,
            String title,
            String author,
            String description,
            Language language,
            Integer publicationYear) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("import_id", importId.toString());
        body.add("owner_id", ownerId.toString());
        body.add("title", title);
        body.add("author", author);
        body.add("description", description == null ? "" : description);
        body.add("language", language.name().toLowerCase());
        if (publicationYear != null) body.add("publication_year", publicationYear.toString());
        try {
            body.add("source_file", new NamedByteArrayResource(source.getBytes(), source.getOriginalFilename()));
        } catch (java.io.IOException exception) {
            throw new IllegalArgumentException("Could not read uploaded book", exception);
        }

        HttpHeaders headers = internalHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        restTemplate.exchange(
                processorUrl + "/internal/imports/", HttpMethod.POST, new HttpEntity<>(body, headers), JsonNode.class);
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
