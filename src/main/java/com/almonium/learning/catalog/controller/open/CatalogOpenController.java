package com.almonium.learning.catalog.controller.open;

import com.almonium.learning.catalog.dto.response.CatalogHit;
import com.almonium.learning.catalog.service.CatalogSearchService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
            @RequestParam @Size(max = 200) String q, @RequestParam(defaultValue = "20") @Min(1) @Max(50) int limit) {
        return ResponseEntity.ok(catalogSearchService.search(q, limit));
    }
}
