package com.almonium.auth.common.repository;

import com.almonium.auth.common.model.entity.Principal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // TODO remove annotations
public interface PrincipalRepository extends JpaRepository<Principal, Long> {
    List<Principal> findByUserId(long userId);
}
