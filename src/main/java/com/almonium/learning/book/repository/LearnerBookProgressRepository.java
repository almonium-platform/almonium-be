package com.almonium.learning.book.repository;

import com.almonium.learning.book.model.entity.LearnerBookProgress;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LearnerBookProgressRepository extends JpaRepository<LearnerBookProgress, UUID> {

    @Modifying
    @Query("delete from LearnerBookProgress p where p.book.id = :bookId and p.learner.user.id = :userId")
    int deleteByUserIdAndBookId(UUID userId, Long bookId);

    @Query("select p from LearnerBookProgress p where p.book.id = :bookId and p.learner.user.id = :userId")
    Optional<LearnerBookProgress> findByUserIdAndBookId(UUID userId, Long bookId);
}
