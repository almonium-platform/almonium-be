package com.almonium.learning.book.repository;

import com.almonium.learning.book.model.entity.LearnerBookProgress;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearnerBookProgressRepository extends JpaRepository<LearnerBookProgress, UUID> {

    void deleteByLearnerIdAndBookId(UUID learnerId, Long bookId);
}
