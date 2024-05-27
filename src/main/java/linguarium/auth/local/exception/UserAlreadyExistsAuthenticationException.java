package linguarium.auth.local.exception;

import org.springframework.security.core.AuthenticationException;

public class UserAlreadyExistsAuthenticationException extends AuthenticationException {
    public UserAlreadyExistsAuthenticationException(String msg) {
        super(msg);
    }
}
