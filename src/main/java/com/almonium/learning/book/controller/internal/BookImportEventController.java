package com.almonium.learning.book.controller.internal;

import com.almonium.learning.book.dto.request.BookImportEventRequest;
import com.almonium.learning.book.service.UserBookImportService;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/internal/books/import-events")
@RequiredArgsConstructor
public class BookImportEventController {
    private static final long MAX_SIGNATURE_AGE_SECONDS = 300;
    private final UserBookImportService importService;
    private final ObjectMapper objectMapper;

    @Value("${app.books.publisher-secret}")
    private String publisherSecret;

    @PostMapping
    public ResponseEntity<Void> event(
            @RequestHeader("X-Almonium-Books-Timestamp") long timestamp,
            @RequestHeader("X-Almonium-Books-Signature") String signature,
            @RequestBody byte[] body) {
        if (Math.abs(System.currentTimeMillis() / 1000 - timestamp) > MAX_SIGNATURE_AGE_SECONDS
                || !MessageDigest.isEqual(
                        hex(hmac(timestamp + ".", body)).getBytes(StandardCharsets.UTF_8),
                        signature.getBytes(StandardCharsets.UTF_8))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        try {
            importService.applyEvent(objectMapper.readValue(body, BookImportEventRequest.class));
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid import event", exception);
        }
    }

    private byte[] hmac(String prefix, byte[] body) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(publisherSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            mac.update(prefix.getBytes(StandardCharsets.UTF_8));
            return mac.doFinal(body);
        } catch (Exception exception) {
            throw new IllegalStateException("Could not verify import event", exception);
        }
    }

    private String hex(byte[] bytes) {
        return java.util.HexFormat.of().formatHex(bytes);
    }
}
