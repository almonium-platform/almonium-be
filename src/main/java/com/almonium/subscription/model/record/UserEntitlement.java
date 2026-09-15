package com.almonium.subscription.model.record;

import com.almonium.subscription.model.entity.enums.Entitlement;
import java.util.UUID;

/** What one user's active plan subscription entitles them to, before any operator grant overrides it. */
public record UserEntitlement(UUID userId, Entitlement entitlement) {}
