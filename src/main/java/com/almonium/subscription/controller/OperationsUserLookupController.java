package com.almonium.subscription.controller;

import com.almonium.subscription.dto.response.OpsUserSummary;
import com.almonium.subscription.repository.AccessGrantRepository;
import com.almonium.subscription.service.EffectiveAccessService;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ops/users")
@RequiredArgsConstructor
public class OperationsUserLookupController {
    private final UserService userService;
    private final EffectiveAccessService effectiveAccessService;
    private final AccessGrantRepository accessGrantRepository;

    @GetMapping
    public ResponseEntity<OpsUserSummary> findByEmail(@RequestParam String email) {
        User user = userService
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("No user with email: " + email));

        OpsUserSummary.ActiveGrant activeGrant = accessGrantRepository
                .findActiveByUserId(user.getId(), Instant.now())
                .map(grant ->
                        new OpsUserSummary.ActiveGrant(grant.getEntitlement(), grant.getExpiresAt(), grant.getReason()))
                .orElse(null);

        return ResponseEntity.ok(new OpsUserSummary(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                effectiveAccessService.entitlementFor(user),
                activeGrant));
    }
}
