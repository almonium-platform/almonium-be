package com.almonium.learning.review.dto;

import java.util.UUID;

public record LeechItemResponse(UUID itemId, String entry, String failedPromptType, int failureCount) {}
