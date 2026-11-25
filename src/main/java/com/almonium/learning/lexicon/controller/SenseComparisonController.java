package com.almonium.learning.lexicon.controller;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.learning.lexicon.dto.request.SenseComparisonRequest;
import com.almonium.learning.lexicon.dto.response.SenseComparisonResponse;
import com.almonium.learning.lexicon.service.SenseComparisonService;
import com.almonium.user.core.model.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * How two dictionary senses differ, in a few sentences. Signed-in accounts only: a new explanation is a model call,
 * and the ceiling on them is counted per member. The side-by-side table itself needs none of this.
 */
@RestController
@RequestMapping("/lexicon/comparisons")
@RequiredArgsConstructor
public class SenseComparisonController {
    private final SenseComparisonService senseComparisonService;

    @PostMapping
    public ResponseEntity<SenseComparisonResponse> compare(
            @Auth User user, @Valid @RequestBody SenseComparisonRequest request) {
        return ResponseEntity.ok(senseComparisonService.compare(user, request));
    }
}
