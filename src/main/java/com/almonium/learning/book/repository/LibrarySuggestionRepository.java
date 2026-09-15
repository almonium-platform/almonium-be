package com.almonium.learning.book.repository;

import com.almonium.learning.book.model.entity.LibrarySuggestion;
import com.almonium.learning.book.model.enums.LibrarySuggestionStatus;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibrarySuggestionRepository extends JpaRepository<LibrarySuggestion, UUID> {
    Optional<LibrarySuggestion> findFirstByBookImportIdAndStatusInOrderByCreatedAtDesc(
            UUID bookImportId, Collection<LibrarySuggestionStatus> statuses);

    boolean existsByBookImportIdAndStatusIn(UUID bookImportId, Collection<LibrarySuggestionStatus> statuses);

    List<LibrarySuggestion> findByStatusIn(Collection<LibrarySuggestionStatus> statuses);

    long countByStatus(LibrarySuggestionStatus status);
}
