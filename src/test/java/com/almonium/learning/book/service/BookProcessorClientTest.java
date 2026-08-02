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
import org.springframework.boot.web.client.RestTemplateBuilder;
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
    void wrapsProcessorHttpFailuresAsApiIntegrationException() {
        RestTemplate restTemplate = new RestTemplate();
        BookProcessorClient client = new BookProcessorClient(restTemplate);
        ReflectionTestUtils.setField(client, "processorUrl", "http://127.0.0.1:1/api/v1");
        ReflectionTestUtils.setField(client, "sharedSecret", "test-secret");

        assertThatThrownBy(() -> client.createPrivateImport(
                        java.util.UUID.randomUUID(),
                        java.util.UUID.randomUUID(),
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
}
