package com.almonium.auth.firebase.repository;

import com.almonium.auth.firebase.model.PendingEmailChange;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingEmailChangeRepository extends JpaRepository<PendingEmailChange, UUID> {
    Optional<PendingEmailChange> findByTokenHash(String tokenHash);
}
