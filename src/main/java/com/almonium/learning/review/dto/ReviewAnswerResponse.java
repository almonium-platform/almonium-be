package com.almonium.learning.review.dto;

import com.almonium.learning.review.model.ReviewOutcome;
import java.time.Instant;
import java.util.UUID;

public record ReviewAnswerResponse(
        UUID eventId,
        ReviewOutcome outcome,
        String answer,
        String expectedAnswer,
        ConfusedItemResponse confusedWith,
        Instant dueAt,
        boolean leech,
        int completedCount,
        int sessionSize) {}
