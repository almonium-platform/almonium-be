package linguarium.auth.common.factory;

import linguarium.auth.common.model.entity.Principal;
import linguarium.auth.local.dto.request.LocalAuthRequest;
import linguarium.auth.local.model.entity.LocalPrincipal;
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
        Principal principal = new LocalPrincipal(user, user.getEmail(), encodedPassword);
        user.getPrincipals().add(principal);
        return principal;
    }
}
