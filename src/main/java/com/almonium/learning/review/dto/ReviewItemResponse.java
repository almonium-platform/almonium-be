package com.almonium.learning.review.dto;

import com.almonium.card.core.model.enums.LearningIntent;
import com.almonium.learning.review.model.ReviewPromptType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ReviewItemResponse(
        UUID itemId,
        UUID promptId,
        LearningIntent intent,
        ReviewPromptType promptType,
        String prompt,
        String sourceContext,
        String language,
        Instant savedAt,
        int seenCount,
        List<ReviewHintResponse> hints) {}
