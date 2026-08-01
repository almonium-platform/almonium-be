package com.almonium.learning.book.repository;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.model.entity.TranslationOrder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface TranslationOrderRepository extends JpaRepository<TranslationOrder, UUID> {
    boolean existsByUserIdAndBookId(UUID userId, UUID bookId);

    Optional<TranslationOrder> findByUserIdAndBookId(UUID userId, UUID bookId);

    List<TranslationOrder> findByBookIdAndLanguage(UUID bookId, Language language);

    @Modifying
    @Transactional
    int deleteByUserIdAndBookIdAndLanguage(UUID id, UUID bookId, Language language);
}
