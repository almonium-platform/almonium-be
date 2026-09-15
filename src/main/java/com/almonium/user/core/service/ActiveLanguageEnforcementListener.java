package com.almonium.user.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.event.EntitlementChangedEvent;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Keeps the active languages inside whatever the account is now entitled to.
 *
 * <p>Restore then enforce, in that order, answers both directions with one path: on a rise the restore fills the new
 * room and the enforcement finds nothing to trim; on a drop there is no room to fill and the enforcement trims.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ActiveLanguageEnforcementListener {
    ActiveLanguageService activeLanguageService;
    UserRepository userRepository;

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void onEntitlementChanged(EntitlementChangedEvent event) {
        User user = userRepository.findById(event.userId()).orElse(null);
        if (user == null) {
            log.warn("Entitlement changed for unknown user {}", event.userId());
            return;
        }
        activeLanguageService.restoreWhatTheDowngradeTook(user);
        activeLanguageService.enforceAllowance(user);
    }
}
