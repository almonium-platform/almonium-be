package com.almonium.auth.common.service;

import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.model.entity.VerificationToken;
import com.almonium.auth.local.model.enums.TokenType;

public interface VerificationTokenManagementService {
    void createAndSendVerificationToken(LocalPrincipal localPrincipal, TokenType tokenType);

    VerificationToken getValidTokenOrThrow(String token, TokenType expectedType);

    void deleteToken(VerificationToken verificationToken);
}
