package com.almonium.infra.chat.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.event.EntitlementChangedEvent;
import com.almonium.user.core.events.UserProfileUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Membership is part of the profile Stream holds - the chat list rings a member's avatar from it - so a plan that
 * starts or ends has to reach Stream the same way a new avatar does.
 *
 * <p>It reaches it by republishing the profile event rather than by calling Stream here: that path is already
 * externalized, retried and after the commit, and one route to the Stream user record cannot drift from itself.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class StreamMembershipListener {
    ApplicationEventPublisher eventPublisher;

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void onEntitlementChanged(EntitlementChangedEvent event) {
        log.info("Entitlement changed for user {}, refreshing their Stream profile", event.userId());
        eventPublisher.publishEvent(new UserProfileUpdatedEvent(event.userId()));
    }
}
