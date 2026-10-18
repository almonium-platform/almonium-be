package com.almonium.learning.lexicon.controller.open;

import com.almonium.learning.lexicon.service.AudioClipService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Streams a pronunciation clip to anyone, like the dictionary itself. A clip never changes once it exists, so the
 * answer is cacheable for a year and marked immutable; the ETag is the hash the clip is stored under. The bytes come
 * through the backend rather than from the bucket so the bucket needs no public face.
 */
@RestController
@RequestMapping("/public/lexicon/clips")
@RequiredArgsConstructor
public class LexiconClipController {
    static final String CACHE_FOR_A_YEAR = "public, max-age=31536000, immutable";
    private static final MediaType MPEG_AUDIO = MediaType.parseMediaType("audio/mpeg");

    private final AudioClipService clipService;

    @GetMapping("/{clipId}")
    public ResponseEntity<byte[]> clip(@PathVariable UUID clipId) {
        return clipService
                .read(clipId)
                .map(content -> ResponseEntity.ok()
                        .contentType(MPEG_AUDIO)
                        .contentLength(content.bytes().length)
                        .header(HttpHeaders.CACHE_CONTROL, CACHE_FOR_A_YEAR)
                        .eTag(content.etag())
                        .body(content.bytes()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
