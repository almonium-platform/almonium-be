package com.almonium.subscription.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.model.entity.AccessGrant;
import com.almonium.subscription.model.entity.enums.Entitlement;
import com.almonium.subscription.repository.AccessGrantRepository;
import com.almonium.user.core.model.entity.User;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EffectiveAccessService {
    AccessGrantRepository accessGrantRepository;
    PlanSubscriptionService planSubscriptionService;

    public Entitlement entitlementFor(User user) {
        return accessGrantRepository
                .findActiveByUserId(user.getId(), Instant.now())
                .map(AccessGrant::getEntitlement)
                .orElseGet(() -> planSubscriptionService.getActivePlan(user).getEntitlement());
    }
}
