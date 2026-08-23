package com.almonium.learning.review.repository;

import com.almonium.card.core.model.entity.LearningItem;
import com.almonium.learning.review.model.ConfusionEdge;
import com.almonium.user.core.model.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfusionEdgeRepository extends JpaRepository<ConfusionEdge, UUID> {
    Optional<ConfusionEdge> findByOwnerAndSourceItemAndTargetItem(
            User owner, LearningItem sourceItem, LearningItem targetItem);
}
