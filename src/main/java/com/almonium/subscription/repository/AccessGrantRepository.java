package com.almonium.subscription.repository;

import com.almonium.subscription.model.entity.AccessGrant;
import com.almonium.subscription.model.record.GrantCount;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AccessGrantRepository extends JpaRepository<AccessGrant, UUID> {

    @Query("""
            select grant from AccessGrant grant
            where grant.user.id = :userId
              and grant.revokedAt is null
              and grant.startsAt <= :now
              and (grant.expiresAt is null or grant.expiresAt > :now)
            order by grant.startsAt desc
            """)
    Optional<AccessGrant> findActiveByUserId(UUID userId, Instant now);

    /** The same rule for a whole list, so a page of people costs one query instead of one per person. */
    @Query("""
            select grant from AccessGrant grant
            where grant.user.id in :userIds
              and grant.revokedAt is null
              and grant.startsAt <= :now
              and (grant.expiresAt is null or grant.expiresAt > :now)
            order by grant.startsAt desc
            """)
    List<AccessGrant> findActiveByUserIds(Collection<UUID> userIds, Instant now);

    /**
     * Revokes whatever is currently active for the user in a single statement, instead of a
     * select-then-save round trip that races under concurrent operator requests.
     */
    @Modifying(clearAutomatically = true)
    @Query("""
            update AccessGrant grant
            set grant.revokedAt = :now
            where grant.user.id = :userId
              and grant.revokedAt is null
            """)
    int revokeActiveByUserId(UUID userId, Instant now);

    /** The grants in force right now, by what they hand out. */
    @Query("""
            select new com.almonium.subscription.model.record.GrantCount(grant.entitlement, count(grant))
            from AccessGrant grant
            where grant.revokedAt is null
              and grant.startsAt <= :now
              and (grant.expiresAt is null or grant.expiresAt > :now)
            group by grant.entitlement
            order by grant.entitlement
            """)
    List<GrantCount> countActiveByEntitlement(Instant now);

    /**
     * People who are members only because an operator said so: an active grant above FREE and no paid plan that is
     * active or running out its cycle.
     */
    @Query("""
            select count(grant) from AccessGrant grant
            where grant.revokedAt is null
              and grant.startsAt <= :now
              and (grant.expiresAt is null or grant.expiresAt > :now)
              and grant.entitlement <> com.almonium.subscription.model.entity.enums.Entitlement.FREE
              and not exists (
                  select ps from PlanSubscription ps
                  where ps.user = grant.user
                    and ps.status in ('ACTIVE', 'ACTIVE_TILL_CYCLE_END')
                    and ps.plan.entitlement <> com.almonium.subscription.model.entity.enums.Entitlement.FREE)
            """)
    long countActivePremiumWithoutPaidPlan(Instant now);
}
