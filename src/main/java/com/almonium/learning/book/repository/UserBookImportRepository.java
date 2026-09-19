package com.almonium.learning.book.repository;

import com.almonium.learning.book.model.entity.UserBookImport;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBookImportRepository extends JpaRepository<UserBookImport, UUID> {
    long countByUserId(UUID userId);

    List<UserBookImport> findByUserIdOrderByCreatedAtDesc(UUID userId);

    Optional<UserBookImport> findByIdAndUserId(UUID id, UUID userId);
}
