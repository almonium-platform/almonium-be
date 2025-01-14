package com.almonium.user.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.model.entity.Principal;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.subscription.mapper.PlanSubscriptionMapper;
import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.subscription.model.entity.enums.PlanFeature;
import com.almonium.subscription.service.PlanSubscriptionService;
import com.almonium.user.core.dto.SubscriptionInfoDto;
import com.almonium.user.core.dto.UserInfo;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.exception.NoPrincipalFoundException;
import com.almonium.user.core.mapper.UserMapper;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.InterestRepository;
import com.almonium.user.core.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Transactional
public class UserService implements UserDetailsService {
    UserRepository userRepository;
    PlanSubscriptionService planSubscriptionService;
    PlanService planService;
    UserMapper userMapper;
    InterestRepository interestRepository;
    PlanSubscriptionMapper planSubscriptionMapper;

    public UserInfo buildUserInfoFromUser(User user) {
        User fetchedUser = getByEmail(user.getEmail());
        PlanSubscription activePlanSubscription = planSubscriptionService.getActiveSub(user);
        Map<PlanFeature, Integer> limits =
                planService.getPlanLimits(activePlanSubscription.getPlan().getId());
        var userInfo = userMapper.userToUserInfo(fetchedUser);
        SubscriptionInfoDto subscriptionInfoDto =
                planSubscriptionMapper.planSubscriptionToPlanDto(activePlanSubscription);
        userInfo.setSubscription(subscriptionInfoDto);
        userInfo.getSubscription().setLimits(limits);
        userInfo.setPremium(
                planService.isPlanPremium(activePlanSubscription.getPlan().getId()));
        return userInfo;
    }

    public User getByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    // TODO fix. Eagerly fetches all user details, may be redundant for some use cases
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    public void changeUsernameById(String username, long id) {
        username = username.toLowerCase();
        User user = getById(id);
        if (user.getUsername().equals(username)) {
            throw new BadUserRequestActionException("Username is already set to: " + username);
        }
        if (!isUsernameAvailable(username)) {
            throw new BadUserRequestActionException("Username is already taken: " + username);
        }
        user.setUsername(username);
        userRepository.save(user);
    }

    public void updateInterests(User user, List<Long> ids) {
        user.setInterests(interestRepository.findAllById(ids));
        userRepository.save(user);
        log.info("User {} updated interests: {}", user.getId(), ids);
    }

    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username.toLowerCase());
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return findByEmail(email)
                .map(user -> {
                    if (user.getPrincipals().isEmpty()) {
                        throw new NoPrincipalFoundException("User exists without any principals: " + email);
                    }

                    return getLocalPrincipal(user)
                            .orElseThrow(() -> new NoPrincipalFoundException(
                                    "Use %s to access your account instead of email and password."
                                            .formatted(collectProvidersNames(user))));
                })
                .orElseThrow(() -> new BadCredentialsException("Email or password are incorrect"));
    }

    public User getUserWithPrincipals(long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
    }

    public Optional<LocalPrincipal> getLocalPrincipal(User user) {
        return user.getPrincipals().stream()
                .filter(principal -> principal instanceof LocalPrincipal)
                // to omit ephemeral principal waiting for email confirmation
                .filter(principal -> principal.getEmail().equals(user.getEmail()))
                .map(principal -> (LocalPrincipal) principal)
                .findFirst();
    }

    public Optional<LocalPrincipal> getUnverifiedLocalPrincipal(User user) {
        return user.getPrincipals().stream()
                .filter(principal -> principal instanceof LocalPrincipal)
                .filter(principal -> !principal.getEmail().equals(user.getEmail()))
                .map(principal -> (LocalPrincipal) principal)
                .findFirst();
    }

    public List<Language> getTargetLanguages(User user) {
        return user.getLearners().stream().map(Learner::getLanguage).toList();
    }

    public void updateFluentLanguages(Set<Language> langs, User user) {
        user.setFluentLangs(new HashSet<>(langs));
        userRepository.save(user);
    }

    private static String collectProvidersNames(User user) {
        return user.getPrincipals().stream()
                .map(Principal::getProvider)
                .map(Enum::name)
                .map(provider -> provider.substring(0, 1).toUpperCase()
                        + provider.substring(1).toLowerCase())
                .collect(Collectors.joining(", "));
    }
}
