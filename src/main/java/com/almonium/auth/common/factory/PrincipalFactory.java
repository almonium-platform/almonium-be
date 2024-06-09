package com.almonium.auth.common.factory;

import com.almonium.auth.local.dto.request.LocalAuthRequest;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.config.security.PasswordEncoder;
import com.almonium.user.core.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrincipalFactory {
    private final PasswordEncoder passwordEncoder;

    public LocalPrincipal createLocalPrincipal(User user, LocalAuthRequest request) {
        LocalPrincipal principal = new LocalPrincipal(user, user.getEmail(), encodePassword(request.password()));
        user.getPrincipals().add(principal);
        return principal;
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
