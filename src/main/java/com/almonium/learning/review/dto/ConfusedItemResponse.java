package com.almonium.learning.review.dto;

import java.util.UUID;

public record ConfusedItemResponse(UUID itemId, String entry, String meaning, String example, int directionCount) {}
