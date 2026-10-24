package com.almonium.learning.catalog.controller.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.almonium.learning.catalog.repository.CatalogStore;
import jakarta.validation.Validation;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
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
class SourceShapeControllerTest {
    private static final String SECRET = "test-publisher-secret";

    @Mock
    CatalogStore store;

    SourceShapeController controller;

    @BeforeEach
    void setUp() {
        controller = new SourceShapeController(
                store,
                new ObjectMapper(),
                Validation.buildDefaultValidatorFactory().getValidator());
        ReflectionTestUtils.setField(controller, "publisherSecret", SECRET);
    }

    @Test
    void aSignedReportIsKeptByDownloadAddress() throws Exception {
        byte[] body = "{\"downloadUrl\":\"https://www.gutenberg.org/ebooks/84.epub3.images\",\"verdict\":\"ok\"}"
                .getBytes(StandardCharsets.UTF_8);
        long now = System.currentTimeMillis() / 1000;

        var response = controller.report(now, sign(now, body), body);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(store).recordShape(eq("https://www.gutenberg.org/ebooks/84.epub3.images"), eq("ok"), any());
    }

    @Test
    void aBadSignatureOrVerdictIsRefused() throws Exception {
        byte[] body = "{\"downloadUrl\":\"https://x/y.epub\",\"verdict\":\"ok\"}".getBytes(StandardCharsets.UTF_8);
        long now = System.currentTimeMillis() / 1000;
        assertThatThrownBy(() -> controller.report(now, "deadbeef", body))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("401");

        byte[] odd = "{\"downloadUrl\":\"https://x/y.epub\",\"verdict\":\"great\"}".getBytes(StandardCharsets.UTF_8);
        assertThatThrownBy(() -> controller.report(now, sign(now, odd), odd))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("400");
        verify(store, never()).recordShape(any(), any(), any());
    }

    static String sign(long timestamp, byte[] body) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        mac.update((timestamp + ".").getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(mac.doFinal(body));
    }
}
