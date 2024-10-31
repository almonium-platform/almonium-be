package com.almonium.auth.common.factory;

import com.almonium.auth.local.dto.request.LocalAuthRequest;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.service.impl.PasswordEncoderService;
import com.almonium.user.core.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrincipalFactory {
    private final PasswordEncoderService passwordEncoderService;

    public LocalPrincipal createLocalPrincipal(LocalPrincipal localPrincipal, String newEmail) {
        User user = localPrincipal.getUser();
        LocalPrincipal newPrincipal = new LocalPrincipal(user, newEmail, localPrincipal.getPassword());
        user.getPrincipals().add(newPrincipal);
        return newPrincipal;
    }

    public LocalPrincipal createLocalPrincipal(User user, LocalAuthRequest request) {
        LocalPrincipal principal =
                new LocalPrincipal(user, request.email(), passwordEncoderService.encodePassword(request.password()));
        user.getPrincipals().add(principal);
        return principal;
    }

    public LocalPrincipal createLocalPrincipal(User user, String password) {
        LocalPrincipal principal =
                new LocalPrincipal(user, user.getEmail(), passwordEncoderService.encodePassword(password));
        user.getPrincipals().add(principal);
        return principal;
    }
}
