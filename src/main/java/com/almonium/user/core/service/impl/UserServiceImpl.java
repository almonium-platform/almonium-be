package com.almonium.user.core.service.impl;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.subscription.service.PlanSubscriptionService;
import com.almonium.subscription.service.StripeApiService;
import com.almonium.user.core.dto.UserInfo;
import com.almonium.user.core.exception.NoPrincipalsFoundException;
import com.almonium.user.core.mapper.UserMapper;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PlanSubscriptionService planSubscriptionService;
    StripeApiService stripeApiService;

    @Override
    public UserInfo buildUserInfoFromUser(User user) {
        return userMapper.userToUserInfo(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteAccount(User user) {
        planSubscriptionService
                .findActiveSubscription(user)
                .ifPresent((activeSub) -> stripeApiService.cancelSubscription(activeSub.getStripeSubscriptionId()));
        userRepository.delete(user);
    }

    @Override
    public void changeUsernameById(String username, long id) {
        userRepository.changeUsername(username, id);
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return (UserDetails) findByEmail(email)
                .map(user -> {
                    if (user.getPrincipals().isEmpty()) {
                        throw new NoPrincipalsFoundException("User exists without any principals: " + email);
                    }

                    return user.getPrincipals().stream()
                            .filter(principal -> principal instanceof LocalPrincipal)
                            .findFirst()
                            .orElseThrow(() -> new NoPrincipalsFoundException(
                                    "User tries to authenticate with local principal, but all his principals are not local: "
                                            + email));
                })
                .orElseThrow(() -> new BadCredentialsException("Email or password are incorrect"));
    }

    @Override
    public User getUserWithPrincipals(long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
    }
}
