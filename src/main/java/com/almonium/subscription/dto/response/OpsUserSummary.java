package com.almonium.subscription.dto.response;

import com.almonium.subscription.model.entity.enums.Entitlement;
import java.time.Instant;
import java.util.UUID;

public record OpsUserSummary(
        UUID id, String email, String username, Entitlement effectiveEntitlement, ActiveGrant activeGrant) {

    public record ActiveGrant(Entitlement entitlement, Instant expiresAt, String reason) {}
}
