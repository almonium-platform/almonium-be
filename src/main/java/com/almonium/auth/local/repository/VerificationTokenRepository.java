package com.almonium.auth.local.repository;

import com.almonium.auth.local.model.entity.VerificationToken;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    void deleteByExpiryDateBefore(LocalDateTime now);
}
