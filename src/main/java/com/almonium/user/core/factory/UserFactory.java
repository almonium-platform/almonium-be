package com.almonium.user.core.factory;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.service.PlanSubscriptionService;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.UsernameGenerator;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class UserFactory {
    PlanSubscriptionService planSubscriptionService;
    UsernameGenerator usernameGenerator;
    UserRepository userRepository;

    public User createUserWithDefaultPlan(String email, boolean emailVerified) {
        String username = usernameGenerator.generateUsername(email);

        User user = User.builder()
                .email(email)
                .username(username)
                .emailVerified(emailVerified)
                .planSubscriptions(new HashSet<>())
                .build();

        userRepository.save(user);
        planSubscriptionService.assignDefaultPlanToUser(user);
        return user;
    }
}
