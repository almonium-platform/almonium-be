package com.almonium.user.core.service.impl;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.enums.PlanFeature;
import com.almonium.subscription.repository.PlanFeatureLimit;
import com.almonium.subscription.repository.PlanLimitRepository;
import com.almonium.subscription.repository.PlanRepository;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class PlanService {
    private static final String DEFAULT_PLAN_NAME = "Free Plan";
    PlanRepository planRepository;
    AtomicReference<Plan> cachedDefaultPlan = new AtomicReference<>();
    PlanLimitRepository PlanLimitRepository;

    public Plan getDefaultPlan() {
        Plan defaultPlan = cachedDefaultPlan.get();
        if (defaultPlan == null) {
            defaultPlan = planRepository
                    .findByName(DEFAULT_PLAN_NAME)
                    .orElseThrow(() -> new IllegalStateException("Default plan not found"));
            cachedDefaultPlan.set(defaultPlan);
        }
        return defaultPlan;
    }

    public Map<PlanFeature, Integer> getPlanLimits(long planId) {
        return PlanLimitRepository.findByPlanId(planId).stream()
                .collect(Collectors.toMap(PlanFeatureLimit::featureKey, PlanFeatureLimit::value));
    }

    public boolean isPlanDefault(long planId) {
        return getDefaultPlan().getId() == planId;
    }
}
