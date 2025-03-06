package com.almonium.auth.local.repository;

import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.model.entity.VerificationToken;
import com.almonium.auth.local.model.enums.TokenType;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {
    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByPrincipalAndTokenTypeIn(LocalPrincipal localPrincipal, Set<TokenType> tokenTypes);

    void deleteByExpiresAtBefore(Instant expiryDate);

    List<VerificationToken> findByTokenTypeAndExpiresAtBefore(TokenType tokenType, Instant expiryDate);
}
