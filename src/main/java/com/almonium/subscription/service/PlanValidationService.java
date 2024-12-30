package com.almonium.subscription.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.exception.PlanValidationException;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.PlanLimit;
import com.almonium.subscription.model.entity.enums.PlanFeature;
import com.almonium.user.core.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class PlanValidationService {
    PlanSubscriptionService subscriptionService;

    public void validatePlanFeature(User user, PlanFeature featureKey, int requestedValue) {
        Plan activePlan = subscriptionService.getActivePlan(user);

        activePlan.getLimits().stream()
                .filter(limit -> limit.getFeatureKey().equals(featureKey))
                .map(PlanLimit::getLimitValue)
                .findFirst()
                .ifPresent(allowedValue -> {
                    if (requestedValue > allowedValue) {
                        throw new PlanValidationException("Plan does not allow this action. Limit: " + allowedValue
                                + ", Required: " + requestedValue);
                    }
                });
    }
}
