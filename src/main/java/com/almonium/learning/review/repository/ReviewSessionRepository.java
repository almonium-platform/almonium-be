package com.almonium.learning.review.repository;

import com.almonium.learning.review.model.ReviewSession;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewSessionRepository extends JpaRepository<ReviewSession, UUID> {
    Optional<ReviewSession> findByIdAndOwnerId(UUID id, UUID ownerId);

    long countByOwnerIdAndCompletedAtIsNotNull(UUID ownerId);
}
