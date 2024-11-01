package com.almonium.auth.local.repository;

import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.model.entity.VerificationToken;
import com.almonium.auth.local.model.enums.TokenType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByPrincipal(LocalPrincipal principal);

    void deleteByExpiryDateBefore(LocalDateTime expiryDate);

    List<VerificationToken> findByTokenTypeAndExpiryDateBefore(TokenType tokenType, LocalDateTime expiryDate);
}
