package com.almonium.subscription.model.record;

import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.PlanSubscription;

/** How many subscriptions sit on one plan in one status. */
public record SubscriptionCount(String plan, Plan.Type cadence, PlanSubscription.Status status, long count) {}
