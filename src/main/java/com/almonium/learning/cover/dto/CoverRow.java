package com.almonium.learning.cover.dto;

import com.almonium.learning.cover.model.CoverStatus;
import java.time.Instant;

/** One work's cover as the operations page lists it: what was chosen, from where, on what licence, and why. */
public record CoverRow(
        String workSlug,
        CoverStatus status,
        String sourcePageUrl,
        String artist,
        String licence,
        String licenceBasis,
        String reason,
        String model,
        Instant checkedAt) {}
