package com.almonium.subscription.repository;

import com.almonium.subscription.model.entity.AccessGrant;
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
}
