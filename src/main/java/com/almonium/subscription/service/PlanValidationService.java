package com.almonium.subscription.service;

import com.almonium.subscription.exception.PlanValidationException;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.PlanLimit;
import com.almonium.subscription.model.entity.enums.PlanFeature;
import com.almonium.user.core.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanValidationService {
    private final PlanSubscriptionService subscriptionService;

    public void validatePlanFeature(User user, PlanFeature featureKey, int requestedValue) {
        Plan activePlan = subscriptionService.getActivePlan(user);

        activePlan.getLimits().stream()
                .filter(limit -> limit.getFeatureKey().equals(featureKey))
                .map(PlanLimit::getValue)
                .findFirst()
                .ifPresent(allowedValue -> {
                    if (requestedValue > allowedValue) {
                        throw new PlanValidationException("Plan does not allow this action. Limit: " + allowedValue
                                + ", Required: " + requestedValue);
                    }
                });
    }
}
