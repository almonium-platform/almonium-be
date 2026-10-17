package com.almonium.learning.lexicon.controller.internal;

import com.almonium.learning.book.controller.internal.BooksSignature;
import com.almonium.learning.lexicon.dto.request.EntryPublication;
import com.almonium.learning.lexicon.dto.response.EntryPublicationResult;
import com.almonium.learning.lexicon.model.EntryDocument;
import com.almonium.learning.lexicon.service.EntrySchema;
import com.almonium.learning.lexicon.service.LexiconPublicationService;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

/**
 * Where the entry writers publish, signed like a book publication. The body is one publication, {@code {writer,
 * entry, meta, built_at}}, or an array of them for the initial load; each entry is checked against the schema and
 * taken in its own transaction, so one bad entry in a batch costs only itself. A single entry answers 400 with the
 * reason when refused; a batch answers 200 with one result per entry.
 */
@Slf4j
@RestController
@RequestMapping("/internal/lexicon/entries")
@RequiredArgsConstructor
public class LexiconPublicationController {
    private static final int MESSAGE_LIMIT = 500;

    private final LexiconPublicationService publicationService;
    private final EntrySchema entrySchema;
    private final BooksSignature signature;
    private final ObjectMapper objectMapper;

    @PutMapping
    public ResponseEntity<?> publish(
            @RequestHeader("X-Almonium-Books-Timestamp") long timestamp,
            @RequestHeader("X-Almonium-Books-Signature") String signed,
            @RequestBody byte[] body) {
        signature.verify(timestamp, signed, body);
        JsonNode root;
        try {
            root = objectMapper.readTree(body);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Malformed entry publication", exception);
        }
        if (root.isArray()) {
            List<EntryPublicationResult> results = new ArrayList<>();
            root.forEach(item -> results.add(publishOne(item)));
            return ResponseEntity.ok(results);
        }
        EntryPublicationResult result = publishOne(root);
        return result.accepted()
                ? ResponseEntity.ok(result)
                : ResponseEntity.badRequest().body(result);
    }

    private EntryPublicationResult publishOne(JsonNode item) {
        JsonNode entry = item.path("entry");
        String language = entry.path("language").asString("");
        String headword = entry.path("headword").asString("");
        if (!item.isObject() || !entry.isObject()) {
            return EntryPublicationResult.rejected(language, headword, "A publication is an object with an entry");
        }
        String writer = item.path("writer").asString("");
        if (writer.isBlank()) {
            return EntryPublicationResult.rejected(language, headword, "writer is required");
        }
        Optional<String> violations = entrySchema.violations(objectMapper.writeValueAsBytes(entry));
        if (violations.isPresent()) {
            return EntryPublicationResult.rejected(language, headword, violations.get());
        }
        EntryPublication publication;
        try {
            publication = new EntryPublication(
                    writer,
                    objectMapper.treeToValue(entry, EntryDocument.class),
                    item.path("meta").isObject()
                            ? objectMapper.convertValue(item.path("meta"), new TypeReference<Map<String, Object>>() {})
                            : Map.of(),
                    builtAt(item.path("built_at")),
                    EntrySchema.VERSION);
        } catch (Exception exception) {
            return EntryPublicationResult.rejected(language, headword, "Malformed publication: " + reason(exception));
        }
        try {
            return publicationService.publish(publication);
        } catch (RuntimeException exception) {
            log.warn("Lexicon entry {}/{} refused", language, headword, exception);
            return EntryPublicationResult.rejected(language, headword, reason(exception));
        }
    }

    private static Instant builtAt(JsonNode node) {
        String text = node.asString("");
        return text.isBlank() ? null : OffsetDateTime.parse(text).toInstant();
    }

    private static String reason(Exception exception) {
        Throwable cause = exception;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        String message = cause.getMessage() == null ? cause.getClass().getSimpleName() : cause.getMessage();
        return message.length() > MESSAGE_LIMIT ? message.substring(0, MESSAGE_LIMIT) : message;
    }
}
