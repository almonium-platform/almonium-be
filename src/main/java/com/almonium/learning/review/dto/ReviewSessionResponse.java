package com.almonium.learning.review.dto;

import java.util.List;
import java.util.UUID;

public record ReviewSessionResponse(UUID sessionId, int backlogCount, List<ReviewItemResponse> items) {}
