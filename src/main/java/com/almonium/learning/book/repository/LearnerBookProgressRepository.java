package com.almonium.learning.book.repository;

import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.model.entity.LearnerBookProgress;
import com.almonium.user.core.model.entity.Learner;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearnerBookProgressRepository extends JpaRepository<LearnerBookProgress, UUID> {

    void deleteByLearnerIdAndBookId(UUID learnerId, Long bookId);

    Optional<LearnerBookProgress> findByLearnerAndBook(Learner learner, Book book);
}
