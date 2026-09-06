package com.almonium.auth.common.service;

import com.almonium.auth.firebase.gateway.FirebaseAuthGateway;
import com.almonium.subscription.service.PlanSubscriptionService;
import com.almonium.user.core.events.UserDeletedEvent;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SensitiveAuthActionsService {
    private final PlanSubscriptionService planSubscriptionService;
    private final FirebaseAuthGateway firebaseAuthGateway;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void deleteAccount(User user) {
        if (user.getFirebaseUid() == null) {
            throw new BadUserRequestActionException("User is not linked to Firebase Authentication");
        }
        Optional<String> billingSubscriptionId = planSubscriptionService.getPaidSubscriptionIdToCancel(user);
        firebaseAuthGateway.deleteUser(user.getFirebaseUid());
        eventPublisher.publishEvent(new UserDeletedEvent(user.getId(), billingSubscriptionId));
        userRepository.delete(user);
        log.info("Deleted Almonium and Firebase identities for user {}", user.getId());
    }
}
