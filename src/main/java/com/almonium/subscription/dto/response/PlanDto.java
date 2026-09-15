package com.almonium.subscription.dto.response;

import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.enums.PlanFeature;
import java.util.Map;

/**
 * A purchasable plan as an unauthenticated visitor sees it. The limits ride along because the pricing card has to
 * state what the plan grants before anyone has bought it, and a client that cannot read them ends up hardcoding the
 * numbers into marketing copy that silently goes stale.
 */
public record PlanDto(
        Long id,
        String name,
        Plan.Type type,
        String description,
        Double price,
        Double founderPrice,
        Map<PlanFeature, Integer> limits) {}
