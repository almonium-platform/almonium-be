package com.almonium.subscription.repository;

import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.user.core.model.entity.User;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface PlanSubscriptionRepository extends JpaRepository<PlanSubscription, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<PlanSubscription> findForUpdateByPaddleSubscriptionId(String paddleSubscriptionId);

    Optional<PlanSubscription> findByUserAndStatusIn(User user, List<PlanSubscription.Status> statuses);

    List<PlanSubscription> findAllByPaddleSubscriptionIdIsNotNullAndStatusIn(List<PlanSubscription.Status> statuses);
}
