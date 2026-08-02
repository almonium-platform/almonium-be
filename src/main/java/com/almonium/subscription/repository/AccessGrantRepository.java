package com.almonium.subscription.repository;

import com.almonium.subscription.model.entity.AccessGrant;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccessGrantRepository extends JpaRepository<AccessGrant, UUID> {

    @Query(
            """
            select grant from AccessGrant grant
            where grant.user.id = :userId
              and grant.revokedAt is null
              and grant.startsAt <= :now
              and (grant.expiresAt is null or grant.expiresAt > :now)
            order by grant.startsAt desc
            """)
    Optional<AccessGrant> findActiveByUserId(UUID userId, Instant now);
}
