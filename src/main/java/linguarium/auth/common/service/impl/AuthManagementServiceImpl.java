package linguarium.auth.common.service.impl;

import static lombok.AccessLevel.PRIVATE;

import linguarium.auth.common.enums.AuthProviderType;
import linguarium.auth.common.exception.AuthMethodNotFoundException;
import linguarium.auth.common.model.entity.Principal;
import linguarium.auth.common.repository.PrincipalRepository;
import linguarium.auth.common.service.AuthManagementService;
import linguarium.auth.common.util.PrincipalFactory;
import linguarium.auth.local.dto.request.LocalAuthRequest;
import linguarium.auth.local.exception.EmailMismatchException;
import linguarium.auth.local.exception.UserAlreadyExistsAuthenticationException;
import linguarium.user.core.model.entity.User;
import linguarium.user.core.service.UserService;
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
    UserService userService;
    PrincipalFactory principalFactory;
    PrincipalRepository principalRepository;

    @Override
    public void linkLocalAuth(Long userId, LocalAuthRequest localAuthRequest) {
        User user = userService.getUserWithPrincipals(userId);
        validateAddLocalAuthRequest(user, localAuthRequest);
        Principal localPrincipal = principalFactory.createLocalPrincipal(user, localAuthRequest);
        principalRepository.save(localPrincipal);
        log.info("Local auth linked for user: {}", userId);
    }

    @Override
    public void unlinkProviderAuth(Long userId, AuthProviderType providerType) {
        User user = userService.getUserWithPrincipals(userId);
        Principal principal = getProviderIfExistsElseThrow(providerType, user);
        user.getPrincipals().remove(principal);
        principalRepository.delete(principal);
        log.info("Provider: {} unlinked for user: {}", providerType, userId);
    }

    private Principal getProviderIfExistsElseThrow(AuthProviderType providerType, User user) {
        return user.getPrincipals().stream()
                .filter(principal -> principal.getProvider() == providerType)
                .findFirst()
                .orElseThrow(() -> new AuthMethodNotFoundException("Auth method not found " + providerType));
    }

    private void validateAddLocalAuthRequest(User user, LocalAuthRequest request) {
        if (user.getPrincipals().stream()
                .anyMatch(principal -> principal.getProvider().equals(AuthProviderType.LOCAL))) {
            throw new UserAlreadyExistsAuthenticationException(
                    "You already have local account registered with " + user.getEmail());
        }
        if (!user.getEmail().equals(request.email())) {
            throw new EmailMismatchException(
                    "You need to register with the email you currently use: " + user.getEmail());
        }
    }
}
