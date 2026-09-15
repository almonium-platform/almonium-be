package com.almonium.learning.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.almonium.analyzer.client.exception.ApiIntegrationException;
import com.almonium.analyzer.translator.model.enums.Language;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

class BookProcessorClientTest {

    @Test
    void sendsMultipartImportsWithContentLength() throws Exception {
        AtomicReference<HttpHeaders> requestHeaders = new AtomicReference<>();
        AtomicReference<byte[]> requestBody = new AtomicReference<>();
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/api/v1/internal/imports/", exchange -> {
            HttpHeaders headers = new HttpHeaders();
            headers.putAll(exchange.getRequestHeaders());
            requestHeaders.set(headers);
            requestBody.set(exchange.getRequestBody().readAllBytes());
            byte[] response = "{}".getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            exchange.sendResponseHeaders(202, response.length);
            try (var output = exchange.getResponseBody()) {
                output.write(response);
            }
        });
        server.start();

        try {
            RestTemplate restTemplate =
                    new com.almonium.config.RestTemplateConfig().restTemplate(new RestTemplateBuilder());
            BookProcessorClient client = new BookProcessorClient(restTemplate);
            ReflectionTestUtils.setField(
                    client,
                    "processorUrl",
                    "http://127.0.0.1:" + server.getAddress().getPort() + "/api/v1");
            ReflectionTestUtils.setField(client, "sharedSecret", "test-secret");

            client.createPrivateImport(
                    java.util.UUID.randomUUID(),
                    java.util.UUID.randomUUID(),
                    "private-reader",
                    new MockMultipartFile("file", "book.xml", "text/xml", "<TEI/>".getBytes(StandardCharsets.UTF_8)),
                    "Title",
                    "Author",
                    "Description",
                    Language.EN,
                    1999);
        } finally {
            server.stop(0);
        }

        assertThat(requestHeaders.get().getContentLength()).isGreaterThan(0);
        assertThat(requestHeaders.get().getFirst(HttpHeaders.TRANSFER_ENCODING)).isNull();
        assertThat(new String(requestBody.get(), StandardCharsets.UTF_8))
                .contains("name=\"source_file\"", "filename=\"book.xml\"", "<TEI/>");
    }

    @Test
    void sendsConfirmedMetadataAsJsonPut() throws Exception {
        AtomicReference<String> requestMethod = new AtomicReference<>();
        AtomicReference<String> requestUri = new AtomicReference<>();
        AtomicReference<byte[]> requestBody = new AtomicReference<>();
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/api/v1/internal/imports/", exchange -> {
            requestMethod.set(exchange.getRequestMethod());
            requestUri.set(exchange.getRequestURI().toString());
            requestBody.set(exchange.getRequestBody().readAllBytes());
            byte[] response = "{}".getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            exchange.sendResponseHeaders(200, response.length);
            try (var output = exchange.getResponseBody()) {
                output.write(response);
            }
        });
        server.start();
        java.util.UUID importId = java.util.UUID.randomUUID();
        java.util.UUID ownerId = java.util.UUID.randomUUID();

        try {
            RestTemplate restTemplate =
                    new com.almonium.config.RestTemplateConfig().restTemplate(new RestTemplateBuilder());
            BookProcessorClient client = new BookProcessorClient(restTemplate);
            ReflectionTestUtils.setField(
                    client,
                    "processorUrl",
                    "http://127.0.0.1:" + server.getAddress().getPort() + "/api/v1");
            ReflectionTestUtils.setField(client, "sharedSecret", "test-secret");

            client.updatePrivateImportMetadata(importId, ownerId, "Title", "Author", null, Language.DE, null);
        } finally {
            server.stop(0);
        }

        assertThat(requestMethod.get()).isEqualTo("PUT");
        assertThat(requestUri.get())
                .isEqualTo("/api/v1/internal/imports/" + importId + "/metadata/?owner_id=" + ownerId);
        assertThat(new String(requestBody.get(), StandardCharsets.UTF_8))
                .contains(
                        "\"title\":\"Title\"",
                        "\"language\":\"de\"",
                        "\"description\":\"\"",
                        "\"publication_year\":null");
    }

    @Test
    void wrapsProcessorHttpFailuresAsApiIntegrationException() {
        RestTemplate restTemplate = new RestTemplate();
        BookProcessorClient client = new BookProcessorClient(restTemplate);
        ReflectionTestUtils.setField(client, "processorUrl", "http://127.0.0.1:1/api/v1");
        ReflectionTestUtils.setField(client, "sharedSecret", "test-secret");

        assertThatThrownBy(() -> client.createPrivateImport(
                        java.util.UUID.randomUUID(),
                        java.util.UUID.randomUUID(),
                        "private-reader",
                        new MockMultipartFile(
                                "file", "book.xml", "text/xml", "<TEI/>".getBytes(StandardCharsets.UTF_8)),
                        "Title",
                        "Author",
                        "Description",
                        Language.EN,
                        1999))
                .isInstanceOf(ApiIntegrationException.class)
                .hasMessage("Book processor request failed");
    }

    @Test
    void readsAnEstimateAndPostsATranslationJobInTheProcessorsShape() throws Exception {
        AtomicReference<String> estimateUri = new AtomicReference<>();
        AtomicReference<byte[]> jobBody = new AtomicReference<>();
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/api/v1/internal/translations/estimate/", exchange -> {
            estimateUri.set(exchange.getRequestURI().toString());
            respond(exchange, 200, """
                {"edition_id": "6f1d7d6a-1c2e-4b1e-9f3a-2f6d1c0a9b11", "edition_slug": "effi-briest-de-original",
                 "target_language": "uk", "tier": "quality", "mode": "batch", "chapters": 36, "blocks": 1420,
                 "source_chars": 612340, "estimated_input_tokens": 190000, "estimated_output_tokens": 210000,
                 "estimated_cost_usd": "1.800000"}
                """);
        });
        server.createContext("/api/v1/internal/translations/", exchange -> {
            jobBody.set(exchange.getRequestBody().readAllBytes());
            respond(exchange, 202, """
                {"edition_id": "0d2c2f2e-5a4b-4c7d-8e9f-0a1b2c3d4e5f", "edition_slug": "effi-briest-uk-parallel",
                 "source_edition_slug": "effi-briest-de-original", "target_language": "uk", "status": "processing",
                 "phase": "translating", "progress": {"completed": 9, "total": 24}, "estimated_cost_usd": "0.412000",
                 "error": "", "started_at": "2026-09-13T12:04:00.123456+00:00", "published_at": null,
                 "published_book_id": null}
                """);
        });
        server.start();

        try {
            BookProcessorClient client = client(server);

            var estimate = client.estimateTranslation("effi-briest-de-original", Language.UK, "quality", "batch");
            var status = client.startTranslation(
                    java.util.UUID.fromString("11111111-2222-3333-4444-555555555555"),
                    "effi-briest-de-original",
                    Language.UK,
                    "quality",
                    "batch");

            assertThat(estimateUri.get())
                    .isEqualTo("/api/v1/internal/translations/estimate/"
                            + "?edition_slug=effi-briest-de-original&target_language=uk&tier=quality&mode=batch");
            assertThat(estimate.chapters()).isEqualTo(36);
            assertThat(estimate.estimatedCostUsd()).isEqualByComparingTo("1.8");
            assertThat(new String(jobBody.get(), StandardCharsets.UTF_8))
                    .contains(
                            "\"job_id\":\"11111111-2222-3333-4444-555555555555\"",
                            "\"target_language\":\"uk\"",
                            "\"auto_publish\":true",
                            "\"register\":\"contemporary neutral\"");
            assertThat(status.phase()).isEqualTo("translating");
            assertThat(status.progressCompleted()).isEqualTo(9);
            assertThat(status.progressTotal()).isEqualTo(24);
            assertThat(status.editionSlug()).isEqualTo("effi-briest-uk-parallel");
            assertThat(status.startedAt()).isEqualTo(java.time.Instant.parse("2026-09-13T12:04:00.123456Z"));
            assertThat(status.publishedBookId()).isNull();
        } finally {
            server.stop(0);
        }
    }

    @Test
    void postsALibraryIngestAndToleratesAnAlreadyForgottenImport() throws Exception {
        AtomicReference<byte[]> ingestBody = new AtomicReference<>();
        AtomicReference<String> deleteMethod = new AtomicReference<>();
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/api/v1/internal/library-ingests/", exchange -> {
            ingestBody.set(exchange.getRequestBody().readAllBytes());
            respond(exchange, 202, """
                {"edition_id": "0d2c2f2e-5a4b-4c7d-8e9f-0a1b2c3d4e5f", "slug": "pending-abc", "status": "queued",
                 "phase": "ingesting", "progress": 5, "error": "", "published_at": null, "published_book_id": null}
                """);
        });
        server.createContext("/api/v1/internal/imports/", exchange -> {
            deleteMethod.set(exchange.getRequestMethod());
            respond(exchange, 404, "{\"detail\": \"Private import not found.\"}");
        });
        server.start();
        java.util.UUID importId = java.util.UUID.randomUUID();
        java.util.UUID ownerId = java.util.UUID.randomUUID();

        try {
            BookProcessorClient client = client(server);

            var status = client.startLibraryIngest(
                    java.util.UUID.randomUUID(),
                    importId,
                    ownerId,
                    "Der Schimmelreiter",
                    "Theodor Storm",
                    null,
                    Language.DE,
                    1888);
            client.deletePrivateImport(importId, ownerId);

            assertThat(new String(ingestBody.get(), StandardCharsets.UTF_8))
                    .contains(
                            "\"import_id\":\"" + importId + "\"",
                            "\"language\":\"de\"",
                            "\"publication_year\":1888",
                            "\"description\":\"\"");
            assertThat(status.phase()).isEqualTo("ingesting");
            assertThat(status.progress()).isEqualTo(5);
            assertThat(deleteMethod.get()).isEqualTo("DELETE");
        } finally {
            server.stop(0);
        }
    }

    private static BookProcessorClient client(HttpServer server) {
        RestTemplate restTemplate =
                new com.almonium.config.RestTemplateConfig().restTemplate(new RestTemplateBuilder());
        BookProcessorClient client = new BookProcessorClient(restTemplate);
        ReflectionTestUtils.setField(
                client,
                "processorUrl",
                "http://127.0.0.1:" + server.getAddress().getPort() + "/api/v1");
        ReflectionTestUtils.setField(client, "sharedSecret", "test-secret");
        return client;
    }

    private static void respond(com.sun.net.httpserver.HttpExchange exchange, int status, String json)
            throws java.io.IOException {
        byte[] response = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        exchange.sendResponseHeaders(status, response.length);
        try (var output = exchange.getResponseBody()) {
            output.write(response);
        }
    }
}
