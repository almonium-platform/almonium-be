package com.almonium.user.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.dto.response.PlanDto;
import com.almonium.subscription.mapper.PlanSubscriptionMapper;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.enums.Entitlement;
import com.almonium.subscription.model.entity.enums.PlanFeature;
import com.almonium.subscription.repository.PlanFeatureLimit;
import com.almonium.subscription.repository.PlanLimitRepository;
import com.almonium.subscription.repository.PlanRepository;
import java.util.EnumMap;
import java.util.List;
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
    private static final String DEFAULT_PLAN_NAME = "FREE";

    AtomicReference<Plan> cachedDefaultPlan = new AtomicReference<>();

    PlanRepository planRepository;
    PlanLimitRepository planLimitRepository;

    PlanSubscriptionMapper planSubscriptionMapper;

    public List<PlanDto> getAvailableRecurringPremiumPlans() {
        Map<Entitlement, Map<PlanFeature, Integer>> limitsByEntitlement = new EnumMap<>(Entitlement.class);
        return planRepository.findAllByTypeInAndActiveTrue(List.of(Plan.Type.MONTHLY, Plan.Type.YEARLY)).stream()
                .map(plan -> withLimits(
                        planSubscriptionMapper.toDto(plan),
                        limitsByEntitlement.computeIfAbsent(plan.getEntitlement(), this::getPlanLimits)))
                .toList();
    }

    private PlanDto withLimits(PlanDto plan, Map<PlanFeature, Integer> limits) {
        return new PlanDto(
                plan.id(), plan.name(), plan.type(), plan.description(), plan.price(), plan.founderPrice(), limits);
    }

    public Map<PlanFeature, Integer> getPlanLimits(Entitlement entitlement) {
        Plan canonicalPlan = planRepository
                .findFirstByEntitlementOrderById(entitlement)
                .orElseThrow(() -> new IllegalStateException("No canonical plan for entitlement " + entitlement));
        return planLimitRepository.findByPlanId(canonicalPlan.getId()).stream()
                .collect(Collectors.toMap(PlanFeatureLimit::featureKey, PlanFeatureLimit::limitValue));
    }

    public Map<PlanFeature, Integer> getPlanLimits(long planId) {
        Plan plan = planRepository.findById(planId).orElseThrow(() -> new IllegalArgumentException("Plan not found"));
        return getPlanLimits(plan.getEntitlement());
    }

    public boolean isPlanDefault(long planId) {
        return getDefaultPlan().getId() == planId;
    }

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
}
