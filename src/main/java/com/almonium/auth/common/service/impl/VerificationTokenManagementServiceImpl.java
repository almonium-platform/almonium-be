package com.almonium.auth.common.service.impl;

import com.almonium.auth.common.service.VerificationTokenManagementService;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.model.entity.VerificationToken;
import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.auth.local.repository.VerificationTokenRepository;
import com.almonium.auth.local.service.TokenGenerator;
import com.almonium.infra.email.dto.EmailDto;
import com.almonium.infra.email.service.AuthTokenEmailComposerService;
import com.almonium.infra.email.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VerificationTokenManagementServiceImpl implements VerificationTokenManagementService {
    private static final int OTP_LENGTH = 6;
    VerificationTokenRepository verificationTokenRepository;
    TokenGenerator tokenGenerator;
    AuthTokenEmailComposerService emailComposerService;
    EmailService emailService;

    @Override
    public VerificationToken createAndSendVerificationToken(LocalPrincipal localPrincipal, TokenType tokenType) {
        String token = tokenGenerator.generateOTP(OTP_LENGTH);
        VerificationToken verificationToken = new VerificationToken(localPrincipal, token, tokenType, 60);
        verificationTokenRepository.save(verificationToken);
        EmailDto emailDto = emailComposerService.composeEmail(localPrincipal.getEmail(), tokenType, token);
        emailService.sendEmail(emailDto);
        return verificationToken;
    }
}
