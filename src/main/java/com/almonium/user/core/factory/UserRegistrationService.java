package com.almonium.user.core.factory;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.service.PlanSubscriptionService;
import com.almonium.user.core.events.UserRegisteredEvent;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.model.enums.SetupStep;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.UsernameGenerator;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserRegistrationService {
    PlanSubscriptionService planSubscriptionService;

    UserRepository userRepository;

    UsernameGenerator usernameGenerator;
    ApplicationEventPublisher eventPublisher;

    @Transactional
    public User createUserWithDefaultPlan(String email, boolean emailVerified) {
        return createUserWithDefaultPlan(email, emailVerified, null);
    }

    @Transactional
    public User createUserWithDefaultPlan(String email, boolean emailVerified, String firebaseUid) {
        String username = usernameGenerator.generateUsername(email);

        User user = User.builder()
                .email(email)
                .firebaseUid(firebaseUid)
                .username(username)
                .emailVerified(emailVerified)
                .planSubscriptions(new HashSet<>())
                .setupStep(SetupStep.getInitial())
                .build();

        // Save the user ONCE to get the ID and persist core data
        User savedUser = userRepository.save(user);

        planSubscriptionService.assignDefaultPlanToUser(savedUser);

        eventPublisher.publishEvent(
                new UserRegisteredEvent(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail()));

        return savedUser;
    }
}
