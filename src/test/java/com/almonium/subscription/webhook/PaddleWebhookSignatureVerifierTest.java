package com.almonium.subscription.webhook;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.almonium.config.properties.PaddleProperties;
import com.almonium.subscription.exception.InvalidPaddleWebhookException;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.HexFormat;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.Test;

class PaddleWebhookSignatureVerifierTest {
    private static final String SECRET = "pdl_ntfset_test_secret";
    private static final Instant NOW = Instant.parse("2026-08-02T12:00:00Z");

    private final PaddleWebhookSignatureVerifier verifier =
            new PaddleWebhookSignatureVerifier(properties(), Clock.fixed(NOW, ZoneOffset.UTC));

    @Test
    void acceptsSignatureForExactRawPayload() throws Exception {
        String payload = "{\"event_type\":\"subscription.created\"}";
        long timestamp = NOW.getEpochSecond();
        String header = "ts=" + timestamp + ";h1=" + signature(timestamp, payload);

        assertThatCode(() -> verifier.verify(payload, header)).doesNotThrowAnyException();
    }

    @Test
    void rejectsModifiedPayload() throws Exception {
        String payload = "{\"value\":1}";
        long timestamp = NOW.getEpochSecond();
        String header = "ts=" + timestamp + ";h1=" + signature(timestamp, payload);

        assertThatThrownBy(() -> verifier.verify("{\"value\":2}", header))
                .isInstanceOf(InvalidPaddleWebhookException.class)
                .hasMessageContaining("Invalid Paddle webhook signature");
    }

    @Test
    void rejectsStaleDeliveryTimestamp() throws Exception {
        String payload = "{}";
        long timestamp = NOW.minusSeconds(6).getEpochSecond();
        String header = "ts=" + timestamp + ";h1=" + signature(timestamp, payload);

        assertThatThrownBy(() -> verifier.verify(payload, header))
                .isInstanceOf(InvalidPaddleWebhookException.class)
                .hasMessageContaining("timestamp");
    }

    private PaddleProperties properties() {
        PaddleProperties properties = new PaddleProperties();
        properties.getWebhook().setSecret(SECRET);
        return properties;
    }

    private String signature(long timestamp, String payload) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        return HexFormat.of().formatHex(mac.doFinal((timestamp + ":" + payload).getBytes(StandardCharsets.UTF_8)));
    }
}
