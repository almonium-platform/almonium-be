package com.almonium.learning.cover.controller.internal;

import com.almonium.learning.book.controller.internal.BooksSignature;
import com.almonium.learning.cover.dto.CoverCandidates;
import com.almonium.learning.cover.dto.CoverDecision;
import com.almonium.learning.cover.model.CoverStatus;
import com.almonium.learning.cover.service.CoverService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

/**
 * Where a judge outside this service chooses covers, signed like a book publication: which works wait, the files
 * one of them may be given, and the decision. The licence rule runs here on the way out and the chosen file is
 * fetched here on the way in, so the judge only ever picks among files that are free to take.
 */
@RestController
@RequestMapping("/internal/covers")
@RequiredArgsConstructor
public class CoverChoiceController {
    private static final int MAX_QUERY_LENGTH = 80;

    private final CoverService coverService;
    private final BooksSignature signature;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    /** The works never looked at. The body is {@code {}}; it is there to be signed. */
    @PostMapping("/pending")
    public ResponseEntity<List<String>> pending(
            @RequestHeader("X-Almonium-Books-Timestamp") long timestamp,
            @RequestHeader("X-Almonium-Books-Signature") String signed,
            @RequestBody byte[] body) {
        signature.verify(timestamp, signed, body);
        return ResponseEntity.ok(coverService.pending());
    }

    /** Every work on the shelf, decided or not: for a run that looks at all of them again. The body is {@code {}}. */
    @PostMapping("/works")
    public ResponseEntity<List<String>> works(
            @RequestHeader("X-Almonium-Books-Timestamp") long timestamp,
            @RequestHeader("X-Almonium-Books-Signature") String signed,
            @RequestBody byte[] body) {
        signature.verify(timestamp, signed, body);
        return ResponseEntity.ok(coverService.works());
    }

    /** {@code {"workSlug": "…"}}: the work's title and author and its licence-cleared candidates. */
    @PostMapping("/candidates")
    public ResponseEntity<CoverCandidates> candidates(
            @RequestHeader("X-Almonium-Books-Timestamp") long timestamp,
            @RequestHeader("X-Almonium-Books-Signature") String signed,
            @RequestBody byte[] body) {
        signature.verify(timestamp, signed, body);
        String workSlug = read(body, JsonNode.class).path("workSlug").asString("");
        if (workSlug.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "workSlug is required");
        }
        return ResponseEntity.ok(coverService.candidates(workSlug));
    }

    /**
     * {@code {"workSlug": "…", "queries": ["Dublin street 1900", …]}}: licence-cleared pictures Commons' search
     * finds for those words, for a work that has no illustrations of its own.
     */
    @PostMapping("/fitting")
    public ResponseEntity<CoverCandidates> fitting(
            @RequestHeader("X-Almonium-Books-Timestamp") long timestamp,
            @RequestHeader("X-Almonium-Books-Signature") String signed,
            @RequestBody byte[] body) {
        signature.verify(timestamp, signed, body);
        JsonNode request = read(body, JsonNode.class);
        String workSlug = request.path("workSlug").asString("");
        List<String> queries = new ArrayList<>();
        request.path("queries").forEach(query -> {
            String words = query.asString("").trim();
            if (!words.isEmpty() && words.length() <= MAX_QUERY_LENGTH) {
                queries.add(words);
            }
        });
        if (workSlug.isBlank() || queries.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "workSlug and queries are required");
        }
        return ResponseEntity.ok(coverService.fitting(workSlug, queries));
    }

    @PutMapping("/decisions")
    public ResponseEntity<Map<String, CoverStatus>> decide(
            @RequestHeader("X-Almonium-Books-Timestamp") long timestamp,
            @RequestHeader("X-Almonium-Books-Signature") String signed,
            @RequestBody byte[] body) {
        signature.verify(timestamp, signed, body);
        CoverDecision decision = read(body, CoverDecision.class);
        Set<ConstraintViolation<CoverDecision>> violations = validator.validate(decision);
        if (!violations.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, violations.iterator().next().getMessage());
        }
        try {
            return ResponseEntity.ok(Map.of("status", coverService.decide(decision)));
        } catch (IllegalArgumentException refused) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, refused.getMessage(), refused);
        }
    }

    private <T> T read(byte[] body, Class<T> type) {
        try {
            return objectMapper.readValue(body, type);
        } catch (Exception malformed) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Malformed request", malformed);
        }
    }
}
