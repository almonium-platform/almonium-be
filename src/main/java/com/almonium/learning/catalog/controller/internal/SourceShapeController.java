package com.almonium.learning.catalog.controller.internal;

import com.almonium.learning.catalog.dto.request.SourceShapeReport;
import com.almonium.learning.catalog.service.TextSources;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Set;
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

/**
 * The book processor tells the catalogue what shape a source file has, signed like its other internal calls, so a
 * work's sources can be ranked on it and a reader's default is a file that was checked.
 */
@RestController
@RequestMapping("/internal/books/sources/shape")
@RequiredArgsConstructor
public class SourceShapeController {
    private static final long MAX_SIGNATURE_AGE_SECONDS = 300;

    private final TextSources textSources;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @Value("${app.books.publisher-secret}")
    private String publisherSecret;

    @PostMapping
    public ResponseEntity<Void> report(
            @RequestHeader("X-Almonium-Books-Timestamp") long timestamp,
            @RequestHeader("X-Almonium-Books-Signature") String signature,
            @RequestBody byte[] body) {
        if (Math.abs(System.currentTimeMillis() / 1000 - timestamp) > MAX_SIGNATURE_AGE_SECONDS
                || !MessageDigest.isEqual(
                        hex(hmac(timestamp + ".", body)).getBytes(StandardCharsets.UTF_8),
                        signature.getBytes(StandardCharsets.UTF_8))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        SourceShapeReport report;
        try {
            report = objectMapper.readValue(body, SourceShapeReport.class);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid shape report", exception);
        }
        Set<ConstraintViolation<SourceShapeReport>> violations = validator.validate(report);
        if (!violations.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, violations.iterator().next().getMessage());
        }
        textSources.recordShape(
                report.downloadUrl(),
                report.verdict(),
                report.checkedAt() == null ? Instant.now() : report.checkedAt());
        return ResponseEntity.noContent().build();
    }

    private byte[] hmac(String prefix, byte[] body) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(publisherSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            mac.update(prefix.getBytes(StandardCharsets.UTF_8));
            return mac.doFinal(body);
        } catch (Exception exception) {
            throw new IllegalStateException("Could not verify the shape report", exception);
        }
    }

    private static String hex(byte[] bytes) {
        return HexFormat.of().formatHex(bytes);
    }
}
