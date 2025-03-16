package com.almonium.learning.book.repository;

import com.almonium.learning.book.model.entity.LearnerBookProgress;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearnerBookProgressRepository extends JpaRepository<LearnerBookProgress, UUID> {
    List<LearnerBookProgress> findByLearnerId(UUID learnerId);

    void deleteByLearnerIdAndBookId(UUID learnerId, UUID bookId);
}
