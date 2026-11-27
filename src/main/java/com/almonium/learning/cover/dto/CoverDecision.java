package com.almonium.learning.cover.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * What a judge decided for one work: the candidate it chose, exactly as it was offered, or none. The focus is where
 * the subject sits in the image, in percent from the left and the top.
 */
public record CoverDecision(
        @NotBlank @Size(max = 180) String workSlug,
        @Valid CoverCandidate chosen,
        @Min(0) @Max(100) int focusX,
        @Min(0) @Max(100) int focusY,
        @Size(max = 1000) String reason,
        @Size(max = 120) String model) {}
