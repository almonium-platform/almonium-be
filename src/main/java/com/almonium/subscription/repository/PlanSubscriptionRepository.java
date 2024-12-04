package com.almonium.subscription.repository;

import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.user.core.model.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanSubscriptionRepository extends JpaRepository<PlanSubscription, UUID> {
    Optional<PlanSubscription> findByStripeSubscriptionId(String stripeSubscriptionId);

    Optional<PlanSubscription> findByUserAndStatusIn(User user, List<PlanSubscription.Status> statuses);
}
