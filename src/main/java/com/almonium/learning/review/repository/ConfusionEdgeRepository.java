package com.almonium.learning.review.repository;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.model.entity.LearningItem;
import com.almonium.learning.review.model.ConfusionEdge;
import com.almonium.user.core.model.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfusionEdgeRepository extends JpaRepository<ConfusionEdge, UUID> {
    Optional<ConfusionEdge> findByOwnerAndSourceItemAndTargetItem(
            User owner, LearningItem sourceItem, LearningItem targetItem);

    /** Every pair this person has mixed up in one language, for the prompt that has to know about them. */
    List<ConfusionEdge> findAllByOwnerAndSourceItemLanguage(User owner, Language language);
}
