package linguarium.auth.common.service.impl;

import static lombok.AccessLevel.PRIVATE;

import linguarium.auth.common.enums.AuthProviderType;
import linguarium.auth.common.exception.AuthMethodNotFoundException;
import linguarium.auth.common.exception.LastAuthMethodException;
import linguarium.auth.common.factory.PrincipalFactory;
import linguarium.auth.common.model.entity.Principal;
import linguarium.auth.common.repository.PrincipalRepository;
import linguarium.auth.common.service.AuthManagementService;
import linguarium.auth.local.dto.request.LocalAuthRequest;
import linguarium.auth.local.exception.EmailMismatchException;
import linguarium.auth.local.exception.UserAlreadyExistsException;
import linguarium.auth.local.model.entity.LocalPrincipal;
import linguarium.auth.local.model.entity.VerificationToken;
import linguarium.auth.local.repository.VerificationTokenRepository;
import linguarium.auth.local.util.TokenGenerator;
import linguarium.user.core.model.entity.User;
import linguarium.user.core.service.UserService;
import linguarium.util.service.EmailService;
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
public class AuthManagementServiceImpl implements AuthManagementService {
    private static final int OTP_LENGTH = 6;
    EmailService emailService;
    UserService userService;
    PrincipalFactory principalFactory;
    PrincipalRepository principalRepository;
    VerificationTokenRepository verificationTokenRepository;

    @Override
    public void linkLocalAuth(Long userId, LocalAuthRequest localAuthRequest) {
        User user = userService.getUserWithPrincipals(userId);
        validateAddLocalAuthRequest(user, localAuthRequest);
        LocalPrincipal localPrincipal = principalFactory.createLocalPrincipal(user, localAuthRequest);
        principalRepository.save(localPrincipal);
        createAndSendVerificationToken(localPrincipal);
        log.info("Local auth for user {} waiting for verification", userId);
    }

    @Override
    public void unlinkAuthMethod(Long userId, AuthProviderType providerType) {
        User user = userService.getUserWithPrincipals(userId);
        Principal principal = getProviderIfPossibleElseThrow(providerType, user);
        user.getPrincipals().remove(principal);
        principalRepository.delete(principal);
        log.info("Provider: {} unlinked for user: {}", providerType, userId);
    }

    @Override
    public void createAndSendVerificationToken(LocalPrincipal localPrincipal) {
        String token = TokenGenerator.generateOTP(OTP_LENGTH);
        VerificationToken verificationToken = new VerificationToken(localPrincipal, token, 60);
        verificationTokenRepository.save(verificationToken);
        emailService.sendVerificationEmail(localPrincipal.getEmail(), verificationToken.getToken());
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

    private void validateAddLocalAuthRequest(User user, LocalAuthRequest request) {
        if (user.getPrincipals().stream()
                .anyMatch(principal -> principal.getProvider().equals(AuthProviderType.LOCAL))) {
            throw new UserAlreadyExistsException("You already have local account registered with " + user.getEmail());
        }
        if (!user.getEmail().equals(request.email())) {
            throw new EmailMismatchException(
                    "You need to register with the email you currently use: " + user.getEmail());
        }
    }
}
