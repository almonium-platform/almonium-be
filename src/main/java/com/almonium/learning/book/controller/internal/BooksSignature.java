package com.almonium.learning.book.controller.internal;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/**
 * The signature the book processor puts on every call to the internal books surface: HMAC-SHA256 of
 * "{timestamp}." and the raw body under the publisher secret, no older than five minutes.
 */
@Component
public class BooksSignature {
    static final long MAX_AGE_SECONDS = 300;

    private final String publisherSecret;

    public BooksSignature(@Value("${app.books.publisher-secret}") String publisherSecret) {
        this.publisherSecret = publisherSecret;
    }

    public void verify(long timestamp, String signature, byte[] body) {
        if (Math.abs(System.currentTimeMillis() / 1000 - timestamp) > MAX_AGE_SECONDS
                || !MessageDigest.isEqual(
                        java.util.HexFormat.of()
                                .formatHex(hmac(timestamp + ".", body))
                                .getBytes(StandardCharsets.UTF_8),
                        signature.getBytes(StandardCharsets.UTF_8))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    private byte[] hmac(String prefix, byte[] body) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(publisherSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            mac.update(prefix.getBytes(StandardCharsets.UTF_8));
            return mac.doFinal(body);
        } catch (Exception exception) {
            throw new IllegalStateException("Cannot verify the books signature", exception);
        }
    }
}
