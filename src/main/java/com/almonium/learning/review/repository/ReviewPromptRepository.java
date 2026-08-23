package com.almonium.learning.review.repository;

import com.almonium.card.core.model.entity.LearningItem;
import com.almonium.card.core.model.enums.LearningIntent;
import com.almonium.learning.review.model.ReviewPrompt;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewPromptRepository extends JpaRepository<ReviewPrompt, UUID> {
    List<ReviewPrompt> findAllByLearningItemAndIntentAndActiveTrueOrderByPosition(
            LearningItem learningItem, LearningIntent intent);
}
