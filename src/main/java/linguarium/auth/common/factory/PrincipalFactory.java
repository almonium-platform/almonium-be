package linguarium.auth.common.factory;

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

    public LocalPrincipal createLocalPrincipal(User user, LocalAuthRequest request) {
        LocalPrincipal principal = new LocalPrincipal(user, user.getEmail(), encodePassword(request.password()));
        user.getPrincipals().add(principal);
        return principal;
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
