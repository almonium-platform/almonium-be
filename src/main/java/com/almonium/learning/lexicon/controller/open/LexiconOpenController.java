package com.almonium.learning.lexicon.controller.open;

import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse;
import com.almonium.learning.lexicon.service.LexiconLookupService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The dictionary, open to guests like the catalogue: what a word means is not personal. The word is bounded like
 * the other public text inputs; the answer is cacheable, since an entry changes only when it is published again.
 */
@Validated
@RestController
@RequestMapping("/public/lexicon")
@RequiredArgsConstructor
public class LexiconOpenController {
    private static final Duration FRESH_FOR = Duration.ofHours(1);

    private final LexiconLookupService lookupService;

    @GetMapping("/{language}/entries")
    public ResponseEntity<LexiconEntryResponse> entries(
            @PathVariable @Pattern(regexp = "^[a-z]{2}$") String language,
            @RequestParam @NotBlank @Size(max = 64) String text) {
        return lookupService
                .lookup(language, text)
                .map(entry -> ResponseEntity.ok()
                        .cacheControl(CacheControl.maxAge(FRESH_FOR).cachePublic())
                        .body(entry))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
