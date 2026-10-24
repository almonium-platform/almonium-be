package com.almonium.learning.lexicon.controller.open;

import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse;
import com.almonium.learning.lexicon.dto.response.LexiconSuggestion;
import com.almonium.learning.lexicon.service.LexiconLookupService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The dictionary, open to guests like the catalogue: what a word means is not personal. The word is bounded like
 * the other public text inputs, and a bad one is a 400: Spring MVC validates the constraints itself, which the
 * global handler answers as a bad request, where a {@code @Validated} proxy would throw past it as a 500. The answer
 * is cacheable, since an entry changes only when it is published again.
 */
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

    /**
     * What the Discover field offers while a word is being typed. {@code fluent} names the language the glosses are
     * in; a guest with none gets rows without glosses.
     */
    @GetMapping("/{language}/suggestions")
    public ResponseEntity<List<LexiconSuggestion>> suggestions(
            @PathVariable @Pattern(regexp = "^[a-z]{2}$") String language,
            @RequestParam @NotBlank @Size(max = 64) String prefix,
            @RequestParam(required = false) @Pattern(regexp = "^[a-z]{2}$") String fluent,
            @RequestParam(defaultValue = "6") @Min(1) @Max(10) int limit) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(FRESH_FOR).cachePublic())
                .body(lookupService.suggest(language, prefix, fluent, limit));
    }
}
