package com.almonium.learning.catalog.controller.open;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.catalog.dto.response.CatalogHit;
import com.almonium.learning.catalog.dto.response.WorkUnlocksView;
import com.almonium.learning.catalog.service.CatalogSearchService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Catalogue search (W1): open to guests, since knowing what exists is not personal. With {@code language}, the shelf's
 * reading language, each hit also says how many readers want it in that language and whether the viewer does.
 */
@Validated
@RestController
@RequestMapping("/public/catalog")
@RequiredArgsConstructor
public class CatalogOpenController {
    private final CatalogSearchService catalogSearchService;

    @GetMapping("/search")
    public ResponseEntity<List<CatalogHit>> search(
            @RequestParam @Size(max = 200) String q,
            @RequestParam(defaultValue = "20") @Min(1) @Max(50) int limit,
            @RequestParam(required = false) Language language) {
        return ResponseEntity.ok(catalogSearchService.search(q, limit, language));
    }

    /**
     * Public Domain Day: the best-known works that become free where we make books on 1 January of {@code year}
     * (next year by default), or became free this January when {@code year} is the current one.
     */
    @GetMapping("/coming-free")
    public ResponseEntity<List<CatalogHit>> comingFree(
            @RequestParam(required = false) @Min(1) @Max(9999) Integer year,
            @RequestParam(defaultValue = "12") @Min(1) @Max(50) int limit,
            @RequestParam(required = false) Language language) {
        int target = year != null ? year : catalogSearchService.currentYear() + 1;
        return ResponseEntity.ok(catalogSearchService.freeFrom(target, limit, language));
    }

    /** Where and when a work is free to read, country by country: the unlock map (W7). */
    @GetMapping("/works/{id}/unlocks")
    public ResponseEntity<WorkUnlocksView> unlocks(@PathVariable UUID id) {
        return ResponseEntity.ok(catalogSearchService.unlocks(id));
    }
}
