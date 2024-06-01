package linguarium.auth.local.service.impl;

import static lombok.AccessLevel.PRIVATE;

import linguarium.auth.common.factory.PrincipalFactory;
import linguarium.auth.common.model.entity.Principal;
import linguarium.auth.common.repository.PrincipalRepository;
import linguarium.auth.local.dto.request.LocalAuthRequest;
import linguarium.auth.local.dto.response.JwtAuthResponse;
import linguarium.auth.local.exception.EmailNotVerifiedException;
import linguarium.auth.local.exception.UserAlreadyExistsException;
import linguarium.auth.local.repository.LocalPrincipalRepository;
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
    UserService userService;
    UserRepository userRepository;
    ProfileService profileService;
    TokenProvider tokenProvider;
    PrincipalFactory principalFactory;
    AuthenticationManager manager;
    PrincipalRepository principalRepository;
    LocalPrincipalRepository localPrincipalRepository;

    @Override
    public JwtAuthResponse login(LocalAuthRequest request) {
        Authentication authentication =
                manager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        if (!localPrincipalRepository.findByEmail(request.email()).isVerified()) {
            throw new EmailNotVerifiedException("Email needs to be verified before logging in.");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Principal account = (Principal) authentication.getPrincipal();
        User user = account.getUser();
        profileService.updateLoginStreak(user.getProfile());
        String jwt = tokenProvider.createToken(authentication);
        return new JwtAuthResponse(jwt, userService.buildUserInfoFromUser(user));
    }

    @Override
    public JwtAuthResponse register(LocalAuthRequest request) {
        validateRegisterRequest(request);
        User user = User.builder().email(request.email()).build();
        Principal account = principalFactory.createLocalPrincipal(user, request);
        userRepository.save(user);
        principalRepository.save(account);
        return login(request);
    }

    private void validateRegisterRequest(LocalAuthRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("User with email " + request.email() + " already exists");
        }
    }
}
