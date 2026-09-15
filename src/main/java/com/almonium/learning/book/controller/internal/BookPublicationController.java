package com.almonium.learning.book.controller.internal;

import com.almonium.learning.book.dto.request.BookPublicationRequest;
import com.almonium.learning.book.dto.request.BookWithdrawalRequest;
import com.almonium.learning.book.dto.response.BookPublicationResponse;
import com.almonium.learning.book.dto.response.BookWithdrawalResponse;
import com.almonium.learning.book.service.BookPublicationService;
import jakarta.validation.Validator;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Comparator;
import java.util.stream.Collectors;
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
@RequestMapping("/internal/books/publications")
@RequiredArgsConstructor
public class BookPublicationController {
    private static final long MAX_SIGNATURE_AGE_SECONDS = 300;
    private final BookPublicationService publicationService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @Value("${app.books.publisher-secret}")
    private String publisherSecret;

    @PostMapping
    public ResponseEntity<BookPublicationResponse> publish(
            @RequestHeader("X-Almonium-Books-Timestamp") long timestamp,
            @RequestHeader("X-Almonium-Books-Signature") String signature,
            @RequestBody byte[] body) {
        verify(timestamp, signature, body);
        BookPublicationRequest request = parse(body, BookPublicationRequest.class, "publication");
        try {
            return ResponseEntity.ok(publicationService.publish(request));
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid publication request", exception);
        }
    }

    @PostMapping("/withdrawals")
    public ResponseEntity<BookWithdrawalResponse> withdraw(
            @RequestHeader("X-Almonium-Books-Timestamp") long timestamp,
            @RequestHeader("X-Almonium-Books-Signature") String signature,
            @RequestBody byte[] body) {
        verify(timestamp, signature, body);
        BookWithdrawalRequest request = parse(body, BookWithdrawalRequest.class, "withdrawal");
        try {
            return ResponseEntity.ok(publicationService.withdraw(request));
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid withdrawal request", exception);
        }
    }

    /**
     * The body is read as raw bytes so the signature covers exactly what was sent, which means
     * Spring never runs bean validation on it. Run it here so a missing field is reported by
     * name instead of surfacing later as a database constraint failure.
     */
    private <T> T parse(byte[] body, Class<T> type, String what) {
        T request;
        try {
            request = objectMapper.readValue(body, type);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Malformed " + what + " request", exception);
        }
        String violations = validator.validate(request).stream()
                .sorted(Comparator.comparing(
                        violation -> violation.getPropertyPath().toString()))
                .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                .collect(Collectors.joining(", "));
        if (!violations.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid " + what + " request: " + violations);
        }
        return request;
    }

    private void verify(long timestamp, String signature, byte[] body) {
        if (Math.abs(System.currentTimeMillis() / 1000 - timestamp) > MAX_SIGNATURE_AGE_SECONDS
                || !MessageDigest.isEqual(
                        hex(hmac(timestamp + ".", body)).getBytes(StandardCharsets.UTF_8),
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
            throw new IllegalStateException("Cannot verify book publication signature", exception);
        }
    }

    private String hex(byte[] value) {
        return java.util.HexFormat.of().formatHex(value);
    }
}
