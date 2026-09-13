package com.almonium.subscription.model.record;

import com.almonium.subscription.model.entity.enums.Entitlement;

/** How many grants currently hand out one entitlement. */
public record GrantCount(Entitlement entitlement, long count) {}
