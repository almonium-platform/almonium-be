package com.almonium.auth.common.service.impl;

import com.almonium.auth.common.service.VerificationTokenManagementService;
import com.almonium.auth.local.exception.InvalidVerificationTokenException;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.model.entity.VerificationToken;
import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.auth.local.repository.VerificationTokenRepository;
import com.almonium.auth.local.service.TokenGenerator;
import com.almonium.infra.email.dto.EmailDto;
import com.almonium.infra.email.service.AuthTokenEmailComposerService;
import com.almonium.infra.email.service.EmailService;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
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
    public void createAndSendVerificationToken(LocalPrincipal localPrincipal, TokenType tokenType) {
        String token = tokenGenerator.generateOTP(OTP_LENGTH);
        VerificationToken verificationToken = new VerificationToken(localPrincipal, token, tokenType, 60);
        verificationTokenRepository.save(verificationToken);
        EmailDto emailDto = emailComposerService.composeEmail(localPrincipal.getEmail(), tokenType, token);
        emailService.sendEmail(emailDto);
        log.info("Verification token sent to {}", localPrincipal.getEmail());
    }

    @Override
    public VerificationToken getValidTokenOrThrow(String token, TokenType expectedType) {
        VerificationToken verificationToken = verificationTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new InvalidVerificationTokenException("Token is invalid or has been used"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidVerificationTokenException("Verification token has expired");
        }

        if (verificationToken.getTokenType() != expectedType) {
            throw new InvalidVerificationTokenException("Invalid token type: should be " + expectedType + " but got "
                    + verificationToken.getTokenType() + " instead");
        }

        return verificationToken;
    }

    @Override
    public void deleteToken(VerificationToken verificationToken) {
        verificationTokenRepository.delete(verificationToken);
    }
}
