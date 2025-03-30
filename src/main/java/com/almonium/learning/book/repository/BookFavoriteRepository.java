package com.almonium.learning.book.repository;

import com.almonium.learning.book.model.entity.BookFavorite;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface BookFavoriteRepository extends JpaRepository<BookFavorite, UUID> {
    Optional<BookFavorite> findByLearnerIdAndBookId(UUID learnerId, Long bookId);

    @Modifying
    int deleteByLearnerIdAndBookId(UUID learnerId, Long bookId);
}