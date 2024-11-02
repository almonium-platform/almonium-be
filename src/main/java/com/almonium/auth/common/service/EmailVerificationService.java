package com.almonium.auth.common.service;

import com.almonium.auth.local.dto.response.VerificationTokenDto;
import java.util.Optional;

public interface EmailVerificationService {
    void sendEmailVerification(long id);

    boolean isEmailVerified(long id);

    Optional<VerificationTokenDto> getLastEmailVerificationToken(long id);
}
