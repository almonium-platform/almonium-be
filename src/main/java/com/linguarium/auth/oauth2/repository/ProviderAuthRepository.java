package com.linguarium.auth.oauth2.repository;

import com.linguarium.auth.oauth2.model.ProviderAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderAuthRepository extends JpaRepository<ProviderAuth, Long> {
}
