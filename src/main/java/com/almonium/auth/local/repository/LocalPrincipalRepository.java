package com.almonium.auth.local.repository;

import com.almonium.auth.local.model.entity.LocalPrincipal;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalPrincipalRepository extends JpaRepository<LocalPrincipal, UUID> {
    Optional<LocalPrincipal> findByEmail(String email);
}
