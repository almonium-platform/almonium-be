package com.almonium.user.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.dto.response.PlanDto;
import com.almonium.subscription.mapper.PlanSubscriptionMapper;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.enums.PlanFeature;
import com.almonium.subscription.repository.PlanFeatureLimit;
import com.almonium.subscription.repository.PlanLimitRepository;
import com.almonium.subscription.repository.PlanRepository;
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
    private static final String INSIDER_PLAN_NAME = "INSIDER";

    AtomicReference<Plan> cachedDefaultPlan = new AtomicReference<>();
    AtomicReference<Plan> cachedInsiderPlan = new AtomicReference<>();

    PlanRepository planRepository;
    PlanLimitRepository planLimitRepository;
    PlanSubscriptionMapper planSubscriptionMapper;

    public List<PlanDto> getAvailableRecurringPremiumPlans() {
        return planSubscriptionMapper.toDto(
                planRepository.findAllByTypeInAndActiveTrue(List.of(Plan.Type.MONTHLY, Plan.Type.YEARLY)));
    }

    public Map<PlanFeature, Integer> getPlanLimits(long planId) {
        return planLimitRepository.findByPlanId(planId).stream()
                .collect(Collectors.toMap(PlanFeatureLimit::featureKey, PlanFeatureLimit::limitValue));
    }

    public boolean isPlanDefault(long planId) {
        return getDefaultPlan().getId() == planId;
    }

    public boolean isPlanInsider(long planId) {
        return getInsiderPlan().getId() == planId;
    }

    public boolean isPlanPremium(Long id) {
        return !isPlanDefault(id); // another option is to check if name is PREMIUM. This one is true for insider plan
    }

    public Plan getInsiderPlan() {
        Plan insiderPlan = cachedInsiderPlan.get();
        if (insiderPlan == null) {
            insiderPlan = planRepository
                    .findByName(INSIDER_PLAN_NAME)
                    .orElseThrow(() -> new IllegalStateException("Insider plan not found"));
            cachedInsiderPlan.set(insiderPlan);
        }
        return insiderPlan;
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
