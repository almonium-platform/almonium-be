package com.almonium.auth.common.service;

import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.model.entity.VerificationToken;
import com.almonium.auth.local.model.enums.TokenType;

public interface VerificationTokenManagementService {
    VerificationToken createAndSendVerificationToken(LocalPrincipal localPrincipal, TokenType tokenType);
}
