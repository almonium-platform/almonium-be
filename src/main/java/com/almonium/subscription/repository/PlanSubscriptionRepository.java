package com.almonium.subscription.repository;

import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.subscription.model.record.UserEntitlement;
import com.almonium.user.core.model.entity.User;
import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface PlanSubscriptionRepository extends JpaRepository<PlanSubscription, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<PlanSubscription> findForUpdateByPaddleSubscriptionId(String paddleSubscriptionId);

    Optional<PlanSubscription> findByUserAndStatusIn(User user, List<PlanSubscription.Status> statuses);

    List<PlanSubscription> findAllByPaddleSubscriptionIdIsNotNullAndStatusIn(List<PlanSubscription.Status> statuses);

    /** Plan-derived entitlements for a whole list, so a page of people costs one query instead of one per person. */
    @Query("""
            select new com.almonium.subscription.model.record.UserEntitlement(ps.user.id, ps.plan.entitlement)
            from PlanSubscription ps
            where ps.user.id in :userIds
              and ps.status in ('ACTIVE', 'ACTIVE_TILL_CYCLE_END')
            """)
    List<UserEntitlement> findActiveEntitlementsByUserIds(Collection<UUID> userIds);
}
