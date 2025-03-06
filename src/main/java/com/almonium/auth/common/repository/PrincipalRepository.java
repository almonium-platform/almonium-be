package com.almonium.auth.common.repository;

import com.almonium.auth.common.model.entity.Principal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrincipalRepository extends JpaRepository<Principal, UUID> {
    List<Principal> findByEmail(String email);
}
