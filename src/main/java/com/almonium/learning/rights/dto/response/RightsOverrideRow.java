package com.almonium.learning.rights.dto.response;

import com.almonium.learning.rights.model.enums.RightsEffect;
import java.time.Instant;
import java.util.UUID;

public record RightsOverrideRow(
        UUID id,
        String workSlug,
        String country,
        RightsEffect effect,
        String reason,
        UUID reportId,
        Instant createdAt) {}
