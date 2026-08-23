package com.almonium.learning.review.dto;

import java.util.List;

public record ReviewSummaryResponse(
        long dueCount,
        int sessionSize,
        long understandCount,
        long produceCount,
        long disambiguateCount,
        long leechCount,
        List<LeechItemResponse> leeches) {}
