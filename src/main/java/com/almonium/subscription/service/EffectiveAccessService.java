package com.almonium.subscription.service;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.model.entity.AccessGrant;
import com.almonium.subscription.model.entity.enums.Entitlement;
import com.almonium.subscription.model.record.UserEntitlement;
import com.almonium.subscription.repository.AccessGrantRepository;
import com.almonium.user.core.model.entity.User;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

/**
 * The one place that answers what a user may actually do.
 *
 * <p>Entitlement has two sources — the plan they pay for and the grant an operator issues — and a grant is a plan in
 * every regard that is not billing. Anything asking a plan directly will disagree with this service the moment a grant
 * exists, so ask here instead: the whole application must give one answer about one person.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EffectiveAccessService {
    AccessGrantRepository accessGrantRepository;
    PlanSubscriptionService planSubscriptionService;

    public Entitlement entitlementFor(User user) {
        return activeGrantFor(user)
                .map(AccessGrant::getEntitlement)
                .orElseGet(() -> planSubscriptionService.getActivePlan(user).getEntitlement());
    }

    public boolean isPremium(User user) {
        return entitlementFor(user) != Entitlement.FREE;
    }

    /**
     * The same answer for a list of people, in two queries rather than two per person. Users with neither a grant nor
     * an active subscription are absent from the map rather than guessed at.
     */
    public Map<UUID, Entitlement> entitlementsFor(Collection<UUID> userIds) {
        if (userIds.isEmpty()) {
            return Map.of();
        }

        Map<UUID, Entitlement> effective = new HashMap<>(planSubscriptionService.getActiveEntitlements(userIds).stream()
                .collect(toMap(UserEntitlement::userId, UserEntitlement::entitlement, (first, second) -> first)));

        // A grant overrides the plan, including downwards: it is the answer whenever there is one.
        accessGrantRepository.findActiveByUserIds(userIds, Instant.now()).stream()
                .collect(
                        toMap(grant -> grant.getUser().getId(), AccessGrant::getEntitlement, (newest, older) -> newest))
                .forEach(effective::put);

        return effective;
    }

    /** Which of these people are members, by the same rule a single lookup would apply. */
    public Set<UUID> premiumAmong(Collection<UUID> userIds) {
        return entitlementsFor(userIds).entrySet().stream()
                .filter(entry -> entry.getValue() != Entitlement.FREE)
                .map(Map.Entry::getKey)
                .collect(toSet());
    }

    /** The grant currently overriding this user's plan, if any. */
    public Optional<AccessGrant> activeGrantFor(User user) {
        return accessGrantRepository.findActiveByUserId(user.getId(), Instant.now());
    }
}
