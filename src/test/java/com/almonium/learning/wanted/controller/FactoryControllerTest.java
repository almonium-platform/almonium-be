package com.almonium.learning.wanted.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.controller.internal.BooksSignature;
import com.almonium.learning.book.dto.response.ProcessorIngestStatus;
import com.almonium.learning.wanted.dto.request.FactoryReservationRequest;
import com.almonium.learning.wanted.dto.response.FactoryReservation;
import com.almonium.learning.wanted.service.FactoryService;
import jakarta.validation.Validation;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class FactoryControllerTest {
    private static final String SECRET = "test-publisher-secret";

    @Mock
    FactoryService factoryService;

    FactoryController controller;

    @BeforeEach
    void setUp() {
        controller = new FactoryController(
                factoryService,
                new BooksSignature(SECRET),
                new ObjectMapper(),
                Validation.buildDefaultValidatorFactory().getValidator());
    }

    @Test
    void reservesAWantedWorkForTheFactory() {
        UUID work = UUID.randomUUID();
        UUID source = UUID.randomUUID();
        UUID preparation = UUID.randomUUID();
        when(factoryService.reserve(new FactoryReservationRequest(work, Language.UK, source)))
                .thenReturn(new FactoryReservation(preparation));
        byte[] body = ("{\"catalogWorkId\":\"" + work + "\",\"language\":\"UK\",\"sourceId\":\"" + source + "\"}")
                .getBytes(StandardCharsets.UTF_8);
        long timestamp = now();

        var response = controller.reserve(timestamp, sign(timestamp, body), body);

        assertThat(response.getBody()).isEqualTo(new FactoryReservation(preparation));
    }

    @Test
    void appliesAPushedStatusAsTheProcessorWritesIt() {
        UUID preparation = UUID.randomUUID();
        byte[] body = ("{\"edition_id\":\"" + UUID.randomUUID() + "\",\"slug\":\"dracula-en\",\"status\":\"ready\","
                        + "\"phase\":\"producing\",\"progress\":40,\"error\":\"\",\"published_at\":null,"
                        + "\"published_book_id\":null,\"completed_stages\":[\"ingest\",\"chapter_analysis\"],"
                        + "\"current_stage\":\"adapt\"}")
                .getBytes(StandardCharsets.UTF_8);
        long timestamp = now();

        var response = controller.status(preparation, timestamp, sign(timestamp, body), body);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        ArgumentCaptor<ProcessorIngestStatus> status = ArgumentCaptor.forClass(ProcessorIngestStatus.class);
        verify(factoryService).report(eq(preparation), status.capture());
        assertThat(status.getValue().phase()).isEqualTo("producing");
        assertThat(status.getValue().completedStages()).containsExactly("ingest", "chapter_analysis");
        assertThat(status.getValue().currentStage()).isEqualTo("adapt");
    }

    @Test
    void refusesABadSignatureAndAnIncompleteReservation() {
        byte[] body = "{}".getBytes(StandardCharsets.UTF_8);
        long timestamp = now();
        assertThatThrownBy(() -> controller.queue(timestamp, "00", body))
                .isInstanceOfSatisfying(
                        ResponseStatusException.class,
                        exception -> assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED));
        assertThatThrownBy(() -> controller.reserve(timestamp, sign(timestamp, body), body))
                .isInstanceOfSatisfying(ResponseStatusException.class, exception -> {
                    assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                    assertThat(exception.getReason()).contains("catalogWorkId");
                });
        verify(factoryService, never()).reserve(any());
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
