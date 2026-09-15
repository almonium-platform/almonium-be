package com.almonium.learning.review.repository;

import com.almonium.learning.review.model.ReviewEvent;
import com.almonium.learning.review.model.ReviewEventId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewEventRepository extends JpaRepository<ReviewEvent, ReviewEventId> {
    Optional<ReviewEvent> findTopByIdAndOwnerIdOrderByReviewedAtDesc(UUID id, UUID ownerId);

    List<ReviewEvent> findAllBySessionIdOrderByReviewedAt(UUID sessionId);

    boolean existsByCorrectsEventIdAndOwnerId(UUID correctsEventId, UUID ownerId);
}
