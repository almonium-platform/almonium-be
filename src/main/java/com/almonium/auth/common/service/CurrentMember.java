package com.almonium.auth.common.service;

import com.almonium.auth.common.model.PrincipalDetails;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * The signed-in member behind the current request, when there is one. For open (/public) endpoints that behave a
 * little differently for members — a report needs no email, a search shows the reader's own wants — where
 * {@code @Auth}, which insists on a member, cannot be used. Session cookie and bearer token both count.
 */
@Component
@RequiredArgsConstructor
public class CurrentMember {
    private final UserRepository userRepository;

    public Optional<UUID> id() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof PrincipalDetails principal) {
            return Optional.ofNullable(principal.getUserId());
        }
        return Optional.empty();
    }

    public Optional<User> find() {
        return id().flatMap(userRepository::findById);
    }
}
