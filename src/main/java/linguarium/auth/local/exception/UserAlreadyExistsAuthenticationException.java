package linguarium.auth.local.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsAuthenticationException extends AuthenticationException {
    public UserAlreadyExistsAuthenticationException(String msg) {
        super(msg);
    }
}
