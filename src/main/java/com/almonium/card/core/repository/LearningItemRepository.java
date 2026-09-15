package com.almonium.card.core.repository;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.model.entity.LearningItem;
import com.almonium.user.core.model.entity.Learner;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningItemRepository extends JpaRepository<LearningItem, UUID> {
    List<LearningItem> findAllByOwner(Learner owner);

    long countByOwner(Learner owner);

    List<LearningItem> findAllByOwnerAndLanguage(Learner owner, Language language);

    void deleteAllByOwnerAndLanguage(Learner owner, Language language);

    List<LearningItem> findAllByOwnerAndEntryLikeIgnoreCase(Learner owner, String entry);

    Optional<LearningItem> findByIdAndOwnerUserId(UUID id, UUID userId);

    Optional<LearningItem> getByPublicId(UUID id);

    List<LearningItem> findAllByOwnerAndNormalizedFormIn(Learner owner, Collection<String> normalizedForms);

    List<LearningItem> findTop10ByOwnerAndLeechFalseAndDueAtLessThanEqualOrderByDueAtAsc(Learner owner, Instant dueAt);

    List<LearningItem> findAllByOwnerAndLeechFalseAndDueAtLessThanEqualOrderByDueAtAsc(Learner owner, Instant dueAt);

    long countByOwnerAndLeechFalseAndDueAtLessThanEqual(Learner owner, Instant dueAt);

    long countByOwnerAndLeechTrue(Learner owner);

    List<LearningItem> findAllByOwnerAndLeechTrueOrderByUpdatedAtAsc(Learner owner);
}
