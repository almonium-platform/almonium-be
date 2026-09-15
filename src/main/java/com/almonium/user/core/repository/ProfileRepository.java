package com.almonium.user.core.repository;

import com.almonium.user.core.model.entity.Profile;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {

    /** One statement by primary key, so stamping presence never loads the profile it stamps. */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Profile p set p.lastSeenAt = :seenAt where p.id = :userId")
    int stampLastSeen(UUID userId, Instant seenAt);

    long countByLastSeenAtGreaterThanEqual(Instant since);
}
