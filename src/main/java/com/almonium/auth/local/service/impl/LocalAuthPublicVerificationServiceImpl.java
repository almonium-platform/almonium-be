package com.almonium.auth.local.service.impl;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.model.entity.Principal;
import com.almonium.auth.common.service.SensitiveAuthActionsService;
import com.almonium.auth.common.service.VerificationTokenManagementService;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.model.entity.VerificationToken;
import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.auth.local.repository.LocalPrincipalRepository;
import com.almonium.auth.local.service.LocalAuthPublicVerificationService;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.UserService;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LocalAuthPublicVerificationServiceImpl implements LocalAuthPublicVerificationService {

    UserService userService;
    VerificationTokenManagementService tokenService;
    SensitiveAuthActionsService sensitiveAuthActionsService;
    PasswordEncoderService passwordEncoderService;

    UserRepository userRepository;
    LocalPrincipalRepository localPrincipalRepository;

    @Override
    public void changeEmail(String token) {
        VerificationToken verificationToken =
                tokenService.validateAndDeleteTokenOrThrow(token, TokenType.EMAIL_CHANGE_VERIFICATION);
        Principal localPrincipal = verificationToken.getPrincipal();
        log.info(
                "Email changed for local authentication method of user: {}",
                localPrincipal.getUser().getEmail());

        long userId = localPrincipal.getUser().getId();
        User user = userService.getUserWithPrincipals(userId);
        user.setEmail(localPrincipal.getEmail());
        user.setEmailVerified(true);
        userRepository.save(user);

        // If app enforces single email per user, unlink all other auth methods with old email
        List<Principal> principalsToUnlink = user.getPrincipals().stream()
                .filter(principal -> !Objects.equals(principal.getEmail(), localPrincipal.getEmail()))
                .toList();

        principalsToUnlink.forEach(
                principal -> sensitiveAuthActionsService.unlinkAuthMethod(userId, principal.getProvider()));
        log.info(
                "{} authentications with old password unlinked for user: {}",
                principalsToUnlink.size(),
                user.getEmail());
    }

    @Override
    public void verifyEmail(String token) {
        VerificationToken verificationToken =
                tokenService.validateAndDeleteTokenOrThrow(token, TokenType.EMAIL_VERIFICATION);
        LocalPrincipal principal = verificationToken.getPrincipal();
        User user = principal.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        VerificationToken verificationToken =
                tokenService.validateAndDeleteTokenOrThrow(token, TokenType.PASSWORD_RESET);
        LocalPrincipal principal = verificationToken.getPrincipal();
        principal.setPassword(passwordEncoderService.encodePassword(newPassword));
        principal.setLastPasswordResetDate(LocalDate.now());
        localPrincipalRepository.save(principal);
    }
}
