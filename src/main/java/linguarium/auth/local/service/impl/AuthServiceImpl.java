package linguarium.auth.local.service.impl;

import static lombok.AccessLevel.PRIVATE;

import linguarium.auth.local.dto.request.LoginRequest;
import linguarium.auth.local.dto.request.RegisterRequest;
import linguarium.auth.local.dto.response.JwtAuthResponse;
import linguarium.auth.local.exception.UserAlreadyExistsAuthenticationException;
import linguarium.auth.local.service.AuthService;
import linguarium.auth.oauth2.model.entity.Principal;
import linguarium.auth.oauth2.repository.PrincipalRepository;
import linguarium.config.security.jwt.TokenProvider;
import linguarium.user.core.model.entity.User;
import linguarium.user.core.repository.UserRepository;
import linguarium.user.core.service.ProfileService;
import linguarium.user.core.service.UserService;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    UserService userService;
    UserRepository userRepository;
    ProfileService profileService;
    TokenProvider tokenProvider;
    PasswordEncoder passwordEncoder;
    AuthenticationManager manager;
    PrincipalRepository principalRepository;

    public AuthServiceImpl(
            UserService userService,
            ProfileService profileService,
            TokenProvider tokenProvider,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            PrincipalRepository principalRepository,
            @Lazy AuthenticationManager manager) {
        this.userService = userService;
        this.profileService = profileService;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.manager = manager;
        this.principalRepository = principalRepository;
    }

    @Override
    public JwtAuthResponse login(LoginRequest request) {
        Authentication authentication =
                manager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Principal account = (Principal) authentication.getPrincipal();
        User user = account.getUser();
        profileService.updateLoginStreak(user.getProfile());
        String jwt = tokenProvider.createToken(authentication);
        return new JwtAuthResponse(jwt, userService.buildUserInfoFromUser(user));
    }

    @Override
    public void register(RegisterRequest request) {
        validateRegisterRequest(request);
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = User.builder().email(request.getEmail()).build();
        Principal account = new Principal(user, encodedPassword);
        userRepository.save(user);
        principalRepository.save(account);
    }

    private void validateRegisterRequest(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsAuthenticationException(
                    "User with email " + request.getEmail() + " already exists");
        }
    }
}
