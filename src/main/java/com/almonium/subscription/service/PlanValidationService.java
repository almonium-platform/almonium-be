package com.almonium.subscription.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.exception.PlanValidationException;
import com.almonium.subscription.model.entity.enums.PlanFeature;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.PlanService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class PlanValidationService {
    EffectiveAccessService effectiveAccessService;
    PlanService planService;

    public void validatePlanFeature(User user, PlanFeature featureKey, int requestedValue) {
        planService.getPlanLimits(effectiveAccessService.entitlementFor(user)).entrySet().stream()
                .filter(limit -> limit.getKey().equals(featureKey))
                .map(java.util.Map.Entry::getValue)
                .findFirst()
                .ifPresent(allowedValue -> {
                    if (allowedValue >= 0 && requestedValue > allowedValue) {
                        throw new PlanValidationException("Plan does not allow this action. Limit: " + allowedValue
                                + ", Required: " + requestedValue);
                    }
                });
    }

    /** An absent value is intentionally unlimited for the effective entitlement. */
    public int effectiveLimit(User user, PlanFeature featureKey) {
        return planService
                .getPlanLimits(effectiveAccessService.entitlementFor(user))
                .getOrDefault(featureKey, -1);
    }
}
