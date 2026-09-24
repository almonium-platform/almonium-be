package com.almonium.auth.common.service;

import com.almonium.auth.firebase.gateway.FirebaseAuthGateway;
import com.almonium.learning.book.service.UserBookImportService;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.service.FoundingMemberService;
import com.almonium.subscription.service.PlanSubscriptionService;
import com.almonium.user.core.events.UserDeletedEvent;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
    private final FoundingMemberService foundingMemberService;
    private final UserBookImportService userBookImportService;
    private final FirebaseAuthGateway firebaseAuthGateway;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void deleteAccount(User user) {
        if (user.getFirebaseUid() == null) {
            throw new BadUserRequestActionException("User is not linked to Firebase Authentication");
        }
        Optional<String> billingSubscriptionId = planSubscriptionService.getPaidSubscriptionIdToCancel(user);
        // Read while the row still exists: the farewell mail is composed after this transaction commits.
        Optional<String> planName = billingSubscriptionId.flatMap(subscription ->
                Optional.ofNullable(planSubscriptionService.getActivePlan(user)).map(Plan::getName));
        String email = user.getEmail();
        String username = user.getUsername();
        List<UUID> bookImportIds = userBookImportService.importIdsOwnedBy(user.getId());

        // Our rows go first and are flushed, so a constraint that refuses the delete fails here, while the Firebase
        // identity still exists and the whole deletion rolls back. The other way round, a refused delete left a
        // person with data we still held and no way to sign in to it. Firebase last: if it fails, the rollback
        // restores the rows it would otherwise have orphaned.
        foundingMemberService.releaseForDeletedAccount(user.getId());
        userRepository.delete(user);
        userRepository.flush();
        firebaseAuthGateway.deleteUser(user.getFirebaseUid());
        eventPublisher.publishEvent(
                new UserDeletedEvent(user.getId(), billingSubscriptionId, email, username, planName, bookImportIds));
        log.info("Deleted Almonium and Firebase identities for user {}", user.getId());
    }
}
