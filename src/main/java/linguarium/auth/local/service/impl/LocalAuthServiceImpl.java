package linguarium.auth.local.service.impl;

import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDateTime;
import linguarium.auth.common.factory.PrincipalFactory;
import linguarium.auth.common.service.AuthManagementService;
import linguarium.auth.local.dto.request.LocalAuthRequest;
import linguarium.auth.local.dto.response.JwtAuthResponse;
import linguarium.auth.local.exception.EmailNotVerifiedException;
import linguarium.auth.local.exception.InvalidTokenException;
import linguarium.auth.local.exception.UserAlreadyExistsException;
import linguarium.auth.local.model.entity.LocalPrincipal;
import linguarium.auth.local.model.entity.VerificationToken;
import linguarium.auth.local.repository.LocalPrincipalRepository;
import linguarium.auth.local.repository.VerificationTokenRepository;
import linguarium.auth.local.service.LocalAuthService;
import linguarium.config.security.jwt.TokenProvider;
import linguarium.user.core.model.entity.User;
import linguarium.user.core.repository.UserRepository;
import linguarium.user.core.service.ProfileService;
import linguarium.user.core.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LocalAuthServiceImpl implements LocalAuthService {
    AuthManagementService authManagementService;
    UserService userService;
    UserRepository userRepository;
    ProfileService profileService;
    TokenProvider tokenProvider;
    PrincipalFactory principalFactory;
    AuthenticationManager manager;
    VerificationTokenRepository verificationTokenRepository;
    LocalPrincipalRepository localPrincipalRepository;

    @Override
    public JwtAuthResponse login(LocalAuthRequest request) {
        Authentication authentication =
                manager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        LocalPrincipal localPrincipal = localPrincipalRepository.findByEmail(request.email());
        if (!localPrincipal.isVerified()) {
            throw new EmailNotVerifiedException("Email needs to be verified before logging in.");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = localPrincipal.getUser();
        profileService.updateLoginStreak(user.getProfile());
        String jwt = tokenProvider.createToken(authentication);
        return new JwtAuthResponse(jwt, userService.buildUserInfoFromUser(user));
    }

    @Override
    public void register(LocalAuthRequest request) {
        validateRegisterRequest(request);
        User user = User.builder().email(request.email()).build();
        LocalPrincipal localPrincipal = principalFactory.createLocalPrincipal(user, request);
        userRepository.save(user);
        localPrincipalRepository.save(localPrincipal);
        authManagementService.createAndSendVerificationToken(localPrincipal);
    }

    @Override
    public void verifyEmail(String token) {
        VerificationToken verificationToken = verificationTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid verification token"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            verificationTokenRepository.delete(verificationToken);
            throw new InvalidTokenException("Verification token has expired");
        }

        LocalPrincipal principal = verificationToken.getPrincipal();
        principal.setVerified(true);
        localPrincipalRepository.save(principal);
        verificationTokenRepository.delete(verificationToken);
    }

    private void validateRegisterRequest(LocalAuthRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("User with email " + request.email() + " already exists");
        }
    }
}
