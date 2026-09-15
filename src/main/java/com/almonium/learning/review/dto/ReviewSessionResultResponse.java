package com.almonium.learning.review.dto;

import java.util.List;

public record ReviewSessionResultResponse(
        int total, int straightThrough, int afterHint, int confused, long stillDue, List<String> dessertSentences) {}
