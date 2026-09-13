package com.almonium.learning.book.controller.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.learning.book.dto.request.BookPublicationRequest;
import com.almonium.learning.book.dto.response.BookPublicationResponse;
import com.almonium.learning.book.service.BookPublicationService;
import jakarta.validation.Validation;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class BookPublicationControllerTest {
    private static final String SECRET = "test-publisher-secret";

    @Mock
    BookPublicationService publicationService;

    BookPublicationController controller;

    @BeforeEach
    void setUp() {
        controller = new BookPublicationController(
                publicationService,
                new ObjectMapper(),
                Validation.buildDefaultValidatorFactory().getValidator());
        ReflectionTestUtils.setField(controller, "publisherSecret", SECRET);
    }

    @Test
    void publishesAValidRequest() {
        UUID bookId = UUID.randomUUID();
        when(publicationService.publish(any(BookPublicationRequest.class)))
                .thenReturn(new BookPublicationResponse(bookId));
        byte[] body = publication("\"cefrLevel\": \"C1\"");
        long timestamp = now();

        var response = controller.publish(timestamp, sign(timestamp, body), body);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().bookId()).isEqualTo(bookId);
    }

    @Test
    void namesTheMissingFieldInsteadOfFailingAtTheDatabase() {
        byte[] body = publication("\"cefrLevel\": null");
        long timestamp = now();

        assertThatThrownBy(() -> controller.publish(timestamp, sign(timestamp, body), body))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(thrown -> {
                    ResponseStatusException exception = (ResponseStatusException) thrown;
                    assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                    assertThat(exception.getReason()).contains("cefrLevel must not be null");
                });
        verify(publicationService, never()).publish(any());
    }

    @Test
    void rejectsMalformedJsonBeforeValidation() {
        byte[] body = "{not json".getBytes(StandardCharsets.UTF_8);
        long timestamp = now();

        assertThatThrownBy(() -> controller.publish(timestamp, sign(timestamp, body), body))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Malformed publication request");
        verify(publicationService, never()).publish(any());
    }

    @Test
    void rejectsABadSignatureBeforeReadingTheBody() {
        byte[] body = publication("\"cefrLevel\": \"C1\"");
        long timestamp = now();

        assertThatThrownBy(() -> controller.publish(timestamp, "deadbeef", body))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(thrown -> assertThat(((ResponseStatusException) thrown).getStatusCode())
                        .isEqualTo(HttpStatus.UNAUTHORIZED));
        verify(publicationService, never()).publish(any());
    }

    private static byte[] publication(String cefrLevelField) {
        String json = """
                {
                  "editionSlug": "frankenstein-en",
                  "sourceHash": "%s",
                  "workSlug": "frankenstein",
                  "title": "Frankenstein",
                  "author": "Mary Shelley",
                  "description": "A scientist creates life.",
                  "originalLanguage": "EN",
                  "language": "EN",
                  "editionType": "original",
                  "sourceEditionSlug": null,
                  "translator": null,
                  "publicationYear": 1818,
                  "coverUrl": null,
                  %s,
                  "wordCount": 75000
                }
                """.formatted("a".repeat(64), cefrLevelField);
        return json.getBytes(StandardCharsets.UTF_8);
    }

    private static long now() {
        return System.currentTimeMillis() / 1000;
    }

    private static String sign(long timestamp, byte[] body) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            mac.update((timestamp + ".").getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(mac.doFinal(body));
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
    }
}
