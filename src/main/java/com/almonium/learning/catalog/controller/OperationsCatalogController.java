package com.almonium.learning.catalog.controller;

import com.almonium.auth.common.annotation.RequireRecentLogin;
import com.almonium.learning.catalog.dto.response.TextSourceView;
import com.almonium.learning.catalog.service.CatalogImportService;
import com.almonium.learning.catalog.service.CatalogImportService.ImportRun;
import com.almonium.learning.catalog.service.TextSources;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** /ops: the catalogue import, which also runs by itself once a month. */
@RestController
@RequestMapping("/ops/catalog")
@RequiredArgsConstructor
public class OperationsCatalogController {
    private final CatalogImportService catalogImportService;
    private final TextSources textSources;

    /** Where a work's text can be fetched, each judged for copyright where we make books. */
    @GetMapping("/works/{catalogWorkId}/sources")
    public ResponseEntity<List<TextSourceView>> sources(@PathVariable UUID catalogWorkId) {
        return ResponseEntity.ok(textSources.forWork(catalogWorkId));
    }

    @GetMapping("/import")
    public ResponseEntity<ImportRun> lastImport() {
        return ResponseEntity.ok(catalogImportService.lastRun());
    }

    @PostMapping("/import")
    @RequireRecentLogin
    public ResponseEntity<ImportRun> startImport() {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(catalogImportService.startImport());
    }
}
