package com.almonium.subscription.repository;

import com.almonium.subscription.model.entity.enums.PlanFeature;

public record PlanFeatureLimit(PlanFeature featureKey, Integer value) {}
