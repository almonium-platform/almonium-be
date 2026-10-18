package com.almonium.learning.lexicon.controller.internal;

import com.almonium.learning.book.controller.internal.BooksSignature;
import com.almonium.learning.lexicon.dto.request.ClipSubmission;
import com.almonium.learning.lexicon.dto.response.ClipSubmissionResult;
import com.almonium.learning.lexicon.dto.response.UnvoicedPronunciation;
import com.almonium.learning.lexicon.service.AudioClipService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;

/**
 * Where the audio phase works: it lists the pronunciations of a language that have no clip yet, synthesises and
 * verifies them, and puts each clip here with its bytes. Both calls are signed like a book publication; the GET
 * signs an empty body.
 */
@RestController
@RequestMapping("/internal/lexicon/pronunciations")
@RequiredArgsConstructor
public class PronunciationClipController {
    public static final int DEFAULT_LIMIT = 500;
    public static final int MAX_LIMIT = 2000;
    private static final Pattern LANGUAGE = Pattern.compile("[a-z]{2}");
    private static final byte[] NO_BODY = new byte[0];

    private final AudioClipService clipService;
    private final BooksSignature signature;
    private final ObjectMapper objectMapper;

    @GetMapping
    public List<UnvoicedPronunciation> list(
            @RequestHeader("X-Almonium-Books-Timestamp") long timestamp,
            @RequestHeader("X-Almonium-Books-Signature") String signed,
            @RequestParam String language,
            @RequestParam String without,
            @RequestParam(defaultValue = "" + DEFAULT_LIMIT) int limit,
            @RequestParam(defaultValue = "0") int offset) {
        signature.verify(timestamp, signed, NO_BODY);
        if (!LANGUAGE.matcher(language).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "language must be a two-letter code");
        }
        if (!"clip".equals(without)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "without must be clip");
        }
        if (limit < 1 || offset < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "limit must be positive and offset not negative");
        }
        return clipService.withoutClips(language, Math.min(limit, MAX_LIMIT), offset);
    }

    @PutMapping("/{id}/clips")
    public ClipSubmissionResult put(
            @RequestHeader("X-Almonium-Books-Timestamp") long timestamp,
            @RequestHeader("X-Almonium-Books-Signature") String signed,
            @PathVariable UUID id,
            @RequestBody byte[] body) {
        signature.verify(timestamp, signed, body);
        ClipSubmission submission;
        try {
            submission = objectMapper.readValue(body, ClipSubmission.class);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Malformed clip submission", exception);
        }
        try {
            return clipService.store(id, submission);
        } catch (EntityNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
    }
}
