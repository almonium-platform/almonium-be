package com.almonium.auth.common.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.exception.AuthMethodNotFoundException;
import com.almonium.auth.common.exception.BadAuthActionRequest;
import com.almonium.auth.common.exception.LastAuthMethodException;
import com.almonium.auth.common.factory.PrincipalFactory;
import com.almonium.auth.common.model.entity.Principal;
import com.almonium.auth.common.model.enums.AuthProviderType;
import com.almonium.auth.common.repository.PrincipalRepository;
import com.almonium.auth.local.dto.request.LocalAuthRequest;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.model.entity.VerificationToken;
import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.auth.local.service.PasswordEncoderService;
import com.almonium.subscription.service.PlanSubscriptionService;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.UserService;
import java.time.LocalDate;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SensitiveAuthActionsService {
    PrincipalFactory principalFactory;
    UserService userService;
    PasswordEncoderService passwordEncoderService;
    PlanSubscriptionService planSubscriptionService;
    VerificationTokenManagementService verificationTokenManagementService;

    UserRepository userRepository;
    PrincipalRepository principalRepository;

    public void changePassword(long id, String newPassword) {
        User user = userService.getById(id);
        LocalPrincipal localPrincipal = userService
                .getLocalPrincipal(user)
                .orElseThrow(
                        () -> new BadAuthActionRequest("Local auth method not found for user: " + user.getEmail()));

        String encodedPassword = passwordEncoderService.encodePassword(newPassword);
        localPrincipal.setPassword(encodedPassword);
        localPrincipal.setLastPasswordResetDate(LocalDate.now());
        principalRepository.save(localPrincipal);
        log.info("Password changed for user: {}", user.getEmail());
    }

    public void requestEmailChange(long id, String newEmail) {
        User user = userService.getById(id);
        LocalPrincipal existingLocalPrincipal = userService
                .getLocalPrincipal(user)
                .orElseThrow(
                        () -> new BadAuthActionRequest("Local auth method not found for user: " + user.getEmail()));

        LocalPrincipal newLocalPrincipal = principalFactory.createLocalPrincipal(existingLocalPrincipal, newEmail);
        newLocalPrincipal = principalRepository.save(newLocalPrincipal);
        verificationTokenManagementService.createAndSendVerificationToken(
                newLocalPrincipal, TokenType.EMAIL_CHANGE_VERIFICATION);
    }

    public void linkLocal(long userId, String password) {
        User user = userService.getUserWithPrincipals(userId);

        if (userService.getLocalPrincipal(user).isPresent()) {
            throw new BadAuthActionRequest("Local auth method already exists for user: " + user.getEmail());
        }

        LocalPrincipal localPrincipal = principalFactory.createLocalPrincipal(user, password);
        principalRepository.save(localPrincipal);
        log.info("Local auth for user {} waiting for verification", userId);
    }

    public void linkLocalWithNewEmail(long id, LocalAuthRequest request) {
        User user = userService.getById(id);
        if (user.getEmail().equals(request.email())) {
            throw new BadAuthActionRequest("You requested to change to the same email: " + user.getEmail());
        }

        if (userService.getUnverifiedLocalPrincipal(user).isPresent()) {
            throw new BadAuthActionRequest(
                    "You already have an unverified email change request. Cancel it to proceed.");
        }

        LocalPrincipal newLocalPrincipal = principalFactory.createLocalPrincipal(user, request);
        newLocalPrincipal = principalRepository.save(newLocalPrincipal);
        verificationTokenManagementService.createAndSendVerificationToken(
                newLocalPrincipal, TokenType.EMAIL_CHANGE_VERIFICATION);
    }

    public void unlinkAuthMethod(long userId, AuthProviderType providerType) {
        User user = userService.getUserWithPrincipals(userId);
        Principal principal = getProviderIfPossibleElseThrow(providerType, user);
        user.getPrincipals().remove(principal);
        principalRepository.delete(principal);
        log.info("Provider: {} unlinked for user: {}", providerType, userId);
    }

    public void deleteAccount(User user) {
        planSubscriptionService.cleanUpPaidSubscriptionsIfAny(user);
        userRepository.delete(user);
    }

    public void handleEmailChangeRequest(long id, Consumer<VerificationToken> action) {
        verificationTokenManagementService
                .findValidEmailVerificationToken(id)
                .ifPresentOrElse(
                        token -> {
                            verificationTokenManagementService.deleteToken(token);
                            action.accept(token); // Perform the specific action (resend or just cancel)
                            log.info("Email change request processed for user: {}", id);
                        },
                        () -> {
                            throw new BadAuthActionRequest(
                                    "No pending email verification or email change request found.");
                        });
    }

    private Principal getProviderIfPossibleElseThrow(AuthProviderType providerType, User user) {
        if (user.getPrincipals().size() == 1) {
            throw new LastAuthMethodException(
                    "Cannot remove the last authentication method for the user: " + user.getEmail());
        }

        return user.getPrincipals().stream()
                .filter(principal -> principal.getProvider() == providerType)
                .findFirst()
                .orElseThrow(() -> new AuthMethodNotFoundException("Auth method not found " + providerType));
    }
}
