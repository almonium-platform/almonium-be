package com.almonium.learning.lexicon.controller.open;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.Duration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The schema of an entry, for the Lexicon API page's download. It is the published schema without the keys that are
 * the pipeline's own (which writer, model and prompt wrote an entry, what ingest dropped or retired): both files are
 * written by {@code scripts/lexicon_research/published_schema.py}, and a test holds this one against the other, so
 * the web client keeps no copy that could fall behind.
 */
@RestController
@RequestMapping("/public/lexicon/schema")
public class LexiconSchemaController {
    public static final String LOCATION = "lexicon/entry.public.schema.json";
    private static final Duration FRESH_FOR = Duration.ofHours(1);

    private final byte[] schema;

    public LexiconSchemaController() {
        try (InputStream source = new ClassPathResource(LOCATION).getInputStream()) {
            schema = source.readAllBytes();
        } catch (IOException exception) {
            throw new UncheckedIOException("Cannot read " + LOCATION, exception);
        }
    }

    @GetMapping
    public ResponseEntity<byte[]> schema() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .cacheControl(CacheControl.maxAge(FRESH_FOR).cachePublic())
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("lexicon-entry.schema.json")
                                .build()
                                .toString())
                .body(schema);
    }
}
