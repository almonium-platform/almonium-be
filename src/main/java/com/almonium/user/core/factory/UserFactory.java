package com.almonium.user.core.factory;

import com.almonium.subscription.service.PlanSubscriptionService;
import com.almonium.user.core.model.entity.User;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFactory {
    private final PlanSubscriptionService planSubscriptionService;

    public User createUserWithDefaultPlan(String email) {
        User user =
                User.builder().email(email).planSubscriptions(new HashSet<>()).build();

        planSubscriptionService.assignDefaultPlanToUser(user);
        return user;
    }
}
