package com.almonium.learning.review.repository;

import com.almonium.learning.review.model.ReviewSessionItem;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewSessionItemRepository extends JpaRepository<ReviewSessionItem, UUID> {
    Optional<ReviewSessionItem> findBySessionIdAndLearningItemId(UUID sessionId, UUID learningItemId);

    List<ReviewSessionItem> findAllBySessionIdOrderByPosition(UUID sessionId);

    long countBySessionIdAndCompletedAtIsNotNull(UUID sessionId);
}
