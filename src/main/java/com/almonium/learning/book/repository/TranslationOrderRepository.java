package com.almonium.learning.book.repository;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.model.entity.TranslationOrder;
import com.almonium.learning.book.model.enums.TranslationOrderStatus;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface TranslationOrderRepository extends JpaRepository<TranslationOrder, UUID> {
    boolean existsByUserIdAndBookIdAndLanguage(UUID userId, UUID bookId, Language language);

    List<TranslationOrder> findByBookIdAndLanguageAndStatus(
            UUID bookId, Language language, TranslationOrderStatus status);

    List<TranslationOrder> findByUserIdAndStatusInOrderByCreatedAtDesc(
            UUID userId, Collection<TranslationOrderStatus> statuses);

    long countByUserIdAndStatusInAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            UUID userId, Collection<TranslationOrderStatus> statuses, Instant from, Instant to);

    @Modifying
    @Transactional
    int deleteByUserIdAndBookIdAndLanguageAndStatus(
            UUID id, UUID bookId, Language language, TranslationOrderStatus status);
}
