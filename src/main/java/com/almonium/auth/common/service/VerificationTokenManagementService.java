package com.almonium.auth.common.service;

import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.model.entity.VerificationToken;
import com.almonium.auth.local.model.enums.TokenType;
import java.util.Optional;

public interface VerificationTokenManagementService {
    Optional<VerificationToken> findValidEmailVerificationToken(long userId);

    void createAndSendVerificationToken(LocalPrincipal localPrincipal, TokenType tokenType);

    VerificationToken validateAndDeleteTokenOrThrow(String token, TokenType expectedType);

    void deleteToken(VerificationToken verificationToken);
}
