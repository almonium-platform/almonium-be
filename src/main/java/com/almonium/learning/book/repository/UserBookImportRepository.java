package com.almonium.learning.book.repository;

import com.almonium.learning.book.model.entity.UserBookImport;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBookImportRepository extends JpaRepository<UserBookImport, UUID> {
    long countByUserIdAndCreatedAtGreaterThanEqual(UUID userId, Instant createdAt);

    List<UserBookImport> findByUserIdOrderByCreatedAtDesc(UUID userId);

    Optional<UserBookImport> findByIdAndUserId(UUID id, UUID userId);
}
