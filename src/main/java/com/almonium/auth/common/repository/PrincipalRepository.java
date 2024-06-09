package com.almonium.auth.common.repository;

import com.almonium.auth.common.model.entity.Principal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrincipalRepository extends JpaRepository<Principal, Long> {}
