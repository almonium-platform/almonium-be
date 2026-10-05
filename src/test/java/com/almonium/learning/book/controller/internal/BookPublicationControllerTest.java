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
import com.almonium.learning.rights.dto.response.ProductionCheck;
import com.almonium.learning.rights.exception.ProductionBlockedException;
import com.almonium.learning.rights.model.RightsStatus;
import com.almonium.learning.rights.service.ProductionGate;
import jakarta.validation.Validation;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.List;
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

    @Mock
    ProductionGate productionGate;

    BookPublicationController controller;

    @BeforeEach
    void setUp() {
        controller = new BookPublicationController(
                publicationService,
                productionGate,
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
    void refusesAWorkUnderCopyrightWhereBooksAreMadeWithTheReason() {
        when(publicationService.publish(any(BookPublicationRequest.class)))
                .thenThrow(new ProductionBlockedException("Under copyright in UA and DE until the end of 2046."));
        byte[] body = publication("\"cefrLevel\": \"C1\"");
        long timestamp = now();

        assertThatThrownBy(() -> controller.publish(timestamp, sign(timestamp, body), body))
                .isInstanceOfSatisfying(ResponseStatusException.class, exception -> {
                    assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
                    assertThat(exception.getReason()).contains("until the end of 2046");
                });
    }

    @Test
    void answersASignedRightsCheck() {
        ProductionCheck check = new ProductionCheck(
                RightsStatus.PROTECTED, 2047, List.of("UA", "DE"), null, null, null, "Under copyright");
        when(productionGate.check("styles", "The Mysterious Affair at Styles", "Agatha Christie", 1920))
                .thenReturn(check);
        byte[] body = """
                {"workSlug": "styles", "title": "The Mysterious Affair at Styles", "author": "Agatha Christie",
                 "publicationYear": 1920}
                """.getBytes(StandardCharsets.UTF_8);
        long timestamp = now();

        assertThat(controller
                        .rightsCheck(timestamp, sign(timestamp, body), body)
                        .getBody())
                .isEqualTo(check);
        assertThatThrownBy(() -> controller.rightsCheck(timestamp, "forged", body))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void carriesTheProcessorEditionAndOurJobIdWhenTheyAreSent() {
        UUID externalJobId = UUID.randomUUID();
        when(publicationService.publish(any(BookPublicationRequest.class)))
                .thenReturn(new BookPublicationResponse(UUID.randomUUID()));
        byte[] body = publication("\"cefrLevel\": \"C1\", \"editionId\": \"%s\", \"externalJobId\": \"%s\""
                .formatted(UUID.randomUUID(), externalJobId));
        long timestamp = now();

        controller.publish(timestamp, sign(timestamp, body), body);

        org.mockito.ArgumentCaptor<BookPublicationRequest> captor =
                org.mockito.ArgumentCaptor.forClass(BookPublicationRequest.class);
        verify(publicationService).publish(captor.capture());
        assertThat(captor.getValue().externalJobId()).isEqualTo(externalJobId);
        assertThat(captor.getValue().editionId()).isNotNull();
    }

    @Test
    void carriesTheAdaptationFloorAndTheReachedLevelsBesideIt() {
        when(publicationService.publish(any(BookPublicationRequest.class)))
                .thenReturn(new BookPublicationResponse(UUID.randomUUID()));
        byte[] body = publication("\"cefrLevel\": \"C1\", \"adaptsTo\": \"B2\", \"reachedLevels\": [\"B2\"]");
        long timestamp = now();

        controller.publish(timestamp, sign(timestamp, body), body);

        org.mockito.ArgumentCaptor<BookPublicationRequest> captor =
                org.mockito.ArgumentCaptor.forClass(BookPublicationRequest.class);
        verify(publicationService).publish(captor.capture());
        assertThat(captor.getValue().adaptsTo()).isEqualTo(com.almonium.analyzer.analyzer.model.enums.CEFR.B2);
        assertThat(captor.getValue().reachedLevels())
                .containsExactly(com.almonium.analyzer.analyzer.model.enums.CEFR.B2);
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
