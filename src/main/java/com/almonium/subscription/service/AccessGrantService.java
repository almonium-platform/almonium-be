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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AccessGrantService {
    AccessGrantRepository accessGrantRepository;

    @Transactional
    public AccessGrant replace(User target, User operator, Entitlement entitlement, Instant expiresAt, String reason) {
        Instant now = Instant.now();
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("An operator reason is required");
        }
        if (expiresAt != null && !expiresAt.isAfter(now)) {
            throw new IllegalArgumentException("Grant expiry must be in the future");
        }
        accessGrantRepository.revokeActiveByUserId(target.getId(), now);

        AccessGrant grant = new AccessGrant();
        grant.setUser(target);
        grant.setGrantedBy(operator);
        grant.setEntitlement(entitlement);
        grant.setReason(reason.trim());
        grant.setStartsAt(now);
        grant.setExpiresAt(expiresAt);
        return accessGrantRepository.save(grant);
    }

    @Transactional
    public void revoke(User target) {
        accessGrantRepository.revokeActiveByUserId(target.getId(), Instant.now());
    }
}
