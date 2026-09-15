package com.almonium.learning.almo.repository;

import com.almonium.card.core.model.entity.LearningItem;
import com.almonium.user.core.model.entity.Learner;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

/** The recently saved half of the queue Almo speaks from; the due half is the review module's own query. */
public interface AlmoQueueRepository extends Repository<LearningItem, UUID> {
    List<LearningItem> findAllByOwnerAndLeechFalseOrderByCreatedAtDesc(Learner owner, Pageable pageable);
}
