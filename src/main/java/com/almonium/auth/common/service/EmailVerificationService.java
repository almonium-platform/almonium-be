package com.almonium.auth.common.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.exception.BadAuthActionRequest;
import com.almonium.auth.common.repository.PrincipalRepository;
import com.almonium.auth.local.dto.response.VerificationTokenDto;
import com.almonium.auth.local.mapper.VerificationTokenMapper;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.model.entity.VerificationToken;
import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.UserService;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EmailVerificationService {
    UserService userService;
    VerificationTokenManagementService tokenService;
    VerificationTokenMapper verificationTokenMapper;
    PrincipalRepository principalRepository;
    SensitiveAuthActionsService sensitiveAuthActionsService;
    VerificationTokenManagementService verificationTokenManagementService;

    @Transactional // TODO why it's needed?
    public void sendEmailVerification(UUID id) {
        User user = userService.getById(id);
        LocalPrincipal localPrincipal = userService
                .getLocalPrincipal(user)
                .orElseThrow(() -> new BadAuthActionRequest(
                        "Email verification is not available without local authentication method"));

        tokenService.createAndSendVerificationTokenIfAllowed(localPrincipal, TokenType.EMAIL_VERIFICATION);
    }

    public Optional<VerificationTokenDto> getLastEmailVerificationToken(UUID id) {
        return tokenService.findValidEmailVerificationToken(id).map(verificationTokenMapper::toDto);
    }

    public void cancelEmailChangeRequest(UUID userId) {
        Consumer<VerificationToken> action = token -> {
            if (TokenType.EMAIL_CHANGE_VERIFICATION.equals(token.getTokenType())) {
                principalRepository.delete(token.getPrincipal());
            }
        };
        sensitiveAuthActionsService.handleEmailChangeRequest(userId, action);
    }

    public void resendEmailVerificationRequest(UUID id) {
        sensitiveAuthActionsService.handleEmailChangeRequest(
                id,
                token -> verificationTokenManagementService.createAndSendVerificationTokenIfAllowed(
                        token.getPrincipal(), token.getTokenType()));
    }
}
