package linguarium.auth.common.util;

import linguarium.auth.common.entity.Principal;
import linguarium.auth.local.dto.request.LocalAuthRequest;
import linguarium.config.security.PasswordEncoder;
import linguarium.user.core.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrincipalFactory {
    private final PasswordEncoder passwordEncoder;

    public Principal createLocalPrincipal(User user, LocalAuthRequest request) {
        String encodedPassword = passwordEncoder.encode(request.password());
        Principal principal = new Principal(user, encodedPassword);
        user.getPrincipals().add(principal);
        return principal;
    }
}
