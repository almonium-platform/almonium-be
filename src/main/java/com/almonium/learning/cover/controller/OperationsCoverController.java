package com.almonium.learning.cover.controller;

import com.almonium.learning.cover.dto.CoverRow;
import com.almonium.learning.cover.service.CoverService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** /ops → covers: what was chosen for each work and why, a way to take one down, and a way to have it looked at again. */
@RestController
@RequestMapping("/ops/covers")
@RequiredArgsConstructor
public class OperationsCoverController {
    private final CoverService coverService;

    @GetMapping
    public ResponseEntity<List<CoverRow>> covers() {
        return ResponseEntity.ok(coverService.rows());
    }

    /** The works the judge has not looked at yet: what the next run of the plan script will take. */
    @GetMapping("/pending")
    public ResponseEntity<List<String>> pending() {
        return ResponseEntity.ok(coverService.pending());
    }

    /** Puts one work back among the pending, so the next run looks again. */
    @PostMapping("/{workSlug}/retry")
    public ResponseEntity<Void> retry(@PathVariable String workSlug) {
        coverService.forget(workSlug);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{workSlug}/reject")
    public ResponseEntity<Void> reject(@PathVariable String workSlug) {
        coverService.reject(workSlug);
        return ResponseEntity.noContent().build();
    }
}
