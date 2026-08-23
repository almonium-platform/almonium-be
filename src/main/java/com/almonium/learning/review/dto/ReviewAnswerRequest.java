package com.almonium.learning.review.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record ReviewAnswerRequest(
        @NotNull UUID promptId, @NotNull String answer, List<String> hintsOpened, boolean revealed) {}
