package com.almonium.learning.stats.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.repository.LearningItemRepository;
import com.almonium.card.core.service.LearnerFinder;
import com.almonium.learning.book.repository.LearnerBookProgressRepository;
import com.almonium.learning.stats.dto.response.LearningStatsResponse;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class LearningStatsService {
    static final int FINISHED_PERCENTAGE = 100;

    LearnerFinder learnerFinder;
    LearningItemRepository learningItemRepository;
    LearnerBookProgressRepository learnerBookProgressRepository;

    public LearningStatsResponse getStats(User user, Language language) {
        Learner learner = learnerFinder.findLearner(user, language);
        return new LearningStatsResponse(
                learningItemRepository.countByOwner(learner),
                learnerBookProgressRepository.countByLearnerIdAndProgressPercentageGreaterThanEqual(
                        learner.getId(), FINISHED_PERCENTAGE));
    }
}
