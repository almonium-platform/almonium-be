package com.almonium.subscription.webhook;

import com.almonium.config.properties.PaddleProperties;
import com.almonium.subscription.exception.InvalidPaddleWebhookException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaddleWebhookSignatureVerifier {
    private static final Duration TIMESTAMP_TOLERANCE = Duration.ofSeconds(5);

    private final PaddleProperties properties;
    private final Clock clock;

    public void verify(String payload, String signatureHeader) {
        SignatureParts parts = parse(signatureHeader);
        Instant signedAt = Instant.ofEpochSecond(parts.timestamp());
        if (Duration.between(signedAt, clock.instant()).abs().compareTo(TIMESTAMP_TOLERANCE) > 0) {
            throw new InvalidPaddleWebhookException("Paddle webhook timestamp is outside the allowed tolerance");
        }

        byte[] expected = hmac(parts.timestamp() + ":" + payload);
        boolean matches = parts.signatures().stream()
                .map(this::decodeHex)
                .anyMatch(candidate -> MessageDigest.isEqual(expected, candidate));
        if (!matches) {
            throw new InvalidPaddleWebhookException("Invalid Paddle webhook signature");
        }
    }

    private SignatureParts parse(String header) {
        if (header == null || header.isBlank()) {
            throw new InvalidPaddleWebhookException("Missing Paddle webhook signature");
        }
        Long timestamp = null;
        List<String> signatures = new ArrayList<>();
        for (String part : header.split(";")) {
            String[] pair = part.trim().split("=", 2);
            if (pair.length != 2) {
                continue;
            }
            if ("ts".equals(pair[0])) {
                try {
                    timestamp = Long.valueOf(pair[1]);
                } catch (NumberFormatException exception) {
                    throw new InvalidPaddleWebhookException("Invalid Paddle webhook timestamp", exception);
                }
            } else if ("h1".equals(pair[0])) {
                signatures.add(pair[1]);
            }
        }
        if (timestamp == null || signatures.isEmpty()) {
            throw new InvalidPaddleWebhookException("Malformed Paddle webhook signature");
        }
        return new SignatureParts(timestamp, signatures);
    }

    private byte[] hmac(String signedPayload) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(
                    properties.getWebhook().getSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return mac.doFinal(signedPayload.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | InvalidKeyException exception) {
            throw new IllegalStateException("HMAC-SHA256 is unavailable", exception);
        }
    }

    private byte[] decodeHex(String value) {
        try {
            return HexFormat.of().parseHex(value);
        } catch (IllegalArgumentException exception) {
            return new byte[0];
        }
    }

    private record SignatureParts(long timestamp, List<String> signatures) {}
}
