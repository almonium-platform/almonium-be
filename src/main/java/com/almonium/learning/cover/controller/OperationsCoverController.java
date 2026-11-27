package com.almonium.learning.cover.controller;

import com.almonium.learning.cover.dto.CoverRow;
import com.almonium.learning.cover.model.CoverStatus;
import com.almonium.learning.cover.service.CoverService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** /ops → covers: what was chosen for each work and why, a way to take one down, and a way to look again. */
@RestController
@RequestMapping("/ops/covers")
@RequiredArgsConstructor
public class OperationsCoverController {
    private final CoverService coverService;

    @GetMapping
    public ResponseEntity<List<CoverRow>> covers() {
        return ResponseEntity.ok(coverService.rows());
    }

    /** Looks for cover art for every work that has never been looked at, in the background. */
    @PostMapping("/run")
    public ResponseEntity<Void> run() {
        Thread.ofVirtual().name("cover-run").start(coverService::prepareMissing);
        return ResponseEntity.accepted().build();
    }

    /** Looks again for one work, replacing what was decided before. */
    @PostMapping("/{workSlug}/retry")
    public ResponseEntity<Map<String, CoverStatus>> retry(@PathVariable String workSlug) {
        return ResponseEntity.ok(Map.of("status", coverService.prepare(workSlug)));
    }

    @PostMapping("/{workSlug}/reject")
    public ResponseEntity<Void> reject(@PathVariable String workSlug) {
        coverService.reject(workSlug);
        return ResponseEntity.noContent().build();
    }
}
