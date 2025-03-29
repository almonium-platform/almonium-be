package com.almonium.learning.book.repository;

import com.almonium.learning.book.model.entity.TranslationOrder;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranslationOrderRepository extends JpaRepository<TranslationOrder, UUID> {
    boolean existsByUserIdAndBookId(UUID userId, Long bookId);

    Optional<TranslationOrder> findByUserIdAndBookId(UUID userId, Long bookId);
}
