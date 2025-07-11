package com.almonium.subscription.dto.response;

import com.almonium.subscription.model.entity.Plan;

public record PlanDto(Long id, String name, Plan.Type type, String description, Double price) {}
