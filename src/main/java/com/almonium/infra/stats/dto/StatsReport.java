package com.almonium.infra.stats.dto;

import com.almonium.subscription.model.record.GrantCount;
import com.almonium.subscription.model.record.SubscriptionCount;
import java.time.Instant;
import java.util.List;

/**
 * How the product is doing, at a glance: who signed up, who came back, who pays, and who is a member because an
 * operator said so. Everything is a count over our own tables; nothing here calls out.
 */
public record StatsReport(
        Instant generatedAt,
        long totalUsers,
        List<Window> windows,
        List<SubscriptionCount> subscriptions,
        List<GrantCount> activeGrants,
        long grantOnlyMembers,
        FoundingMembers foundingMembers) {

    /** Sign-ups and returning users since {@code since}, which starts today's UTC day or a whole number of days ago. */
    public record Window(String label, Instant since, long registered, long active) {}

    /** How the founding-member slots stand. */
    public record FoundingMembers(long confirmed, long reserved, long available) {}
}
