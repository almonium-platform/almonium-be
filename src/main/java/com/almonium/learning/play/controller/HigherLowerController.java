package com.almonium.learning.play.controller;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.annotation.Auth;
import com.almonium.learning.play.dto.request.HigherLowerRun;
import com.almonium.learning.play.dto.response.HigherLowerBest;
import com.almonium.learning.play.dto.response.HigherLowerRunResult;
import com.almonium.learning.play.dto.response.HigherLowerWord;
import com.almonium.learning.play.service.HigherLowerService;
import com.almonium.user.core.model.entity.User;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Higher or Lower. The words are public so a guest can play; a signed-in member's best run per language is kept.
 * {@code GET /public/play/higher-lower/{language}/words} answers a fresh random batch each call, so it is not cached.
 */
@RestController
@RequiredArgsConstructor
public class HigherLowerController {
    private final HigherLowerService higherLowerService;

    @GetMapping("/public/play/higher-lower/{language}/words")
    public ResponseEntity<List<HigherLowerWord>> words(
            @PathVariable Language language, @RequestParam(required = false) Language fluent) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(higherLowerService.words(language, fluent));
    }

    @GetMapping("/play/higher-lower/bests")
    public ResponseEntity<List<HigherLowerBest>> bests(@Auth User user) {
        return ResponseEntity.ok(higherLowerService.bests(user));
    }

    @PutMapping("/play/higher-lower/bests/{language}")
    public ResponseEntity<HigherLowerRunResult> record(
            @Auth User user, @PathVariable Language language, @Valid @RequestBody HigherLowerRun run) {
        return ResponseEntity.ok(higherLowerService.record(user, language, run.length()));
    }
}
