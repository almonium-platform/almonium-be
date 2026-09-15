package com.almonium.subscription.event;

import java.util.UUID;

/**
 * What a user is entitled to has moved — a plan started or ended, or an operator grant was issued or withdrawn.
 *
 * <p>In-process and inside the caller's transaction on purpose: the rules that depend on entitlement, such as how
 * many languages may stay active, must be true the instant the plan row is. Published only at settled points, never
 * mid-transition, or a downgrade-then-upgrade would briefly look like a downgrade.
 */
public record EntitlementChangedEvent(UUID userId) {}
