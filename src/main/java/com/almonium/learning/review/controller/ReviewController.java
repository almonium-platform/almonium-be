package com.almonium.learning.review.controller;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.annotation.Auth;
import com.almonium.learning.review.dto.ReviewAnswerRequest;
import com.almonium.learning.review.dto.ReviewAnswerResponse;
import com.almonium.learning.review.dto.ReviewSessionResponse;
import com.almonium.learning.review.dto.ReviewSessionResultResponse;
import com.almonium.learning.review.dto.ReviewSummaryResponse;
import com.almonium.learning.review.service.ReviewService;
import com.almonium.user.core.model.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Review")
@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/summary/{language}")
    public ReviewSummaryResponse summary(@PathVariable Language language, @Auth User user) {
        return reviewService.getSummary(user, language);
    }

    @PostMapping("/sessions/{language}")
    public ReviewSessionResponse startSession(@PathVariable Language language, @Auth User user) {
        return reviewService.startSession(user, language);
    }

    @PostMapping("/sessions/{sessionId}/items/{itemId}/answer")
    public ReviewAnswerResponse answer(
            @PathVariable UUID sessionId,
            @PathVariable UUID itemId,
            @Valid @RequestBody ReviewAnswerRequest request,
            @Auth User user) {
        return reviewService.answer(user, sessionId, itemId, request);
    }

    @GetMapping("/sessions/{sessionId}/result")
    public ReviewSessionResultResponse result(@PathVariable UUID sessionId, @Auth User user) {
        return reviewService.sessionResult(user, sessionId);
    }

    @PostMapping("/events/{eventId}/mistype")
    public ResponseEntity<Void> markMistype(@PathVariable UUID eventId, @Auth User user) {
        reviewService.markMistype(user, eventId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/leeches/{itemId}/reencounter")
    public ResponseEntity<Void> reencounter(@PathVariable UUID itemId, @Auth User user) {
        reviewService.reencounter(user, itemId);
        return ResponseEntity.noContent().build();
    }
}
