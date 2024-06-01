package linguarium.config;

import java.util.HashMap;
import java.util.Map;
import linguarium.auth.common.exception.AuthMethodNotFoundException;
import linguarium.auth.common.exception.LastAuthMethodException;
import linguarium.auth.local.exception.EmailMismatchException;
import linguarium.auth.local.exception.UserAlreadyExistsException;
import linguarium.user.core.exception.NoPrincipalsFoundException;
import linguarium.user.friendship.exception.FriendshipNotAllowedException;
import linguarium.util.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({InternalAuthenticationServiceException.class})
    public ResponseEntity<?> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler({BadCredentialsException.class, IllegalAccessException.class})
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex) {
        log.error("An error occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, ex.getMessage()));
    }

    // custom exceptions
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFoundException(NoResourceFoundException ignored) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FriendshipNotAllowedException.class)
    public ResponseEntity<?> handleFriendshipNotAllowedException(FriendshipNotAllowedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, ex.getMessage()));
    }

    // auth
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(EmailMismatchException.class)
    public ResponseEntity<Object> handleEmailMismatchException(EmailMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(NoPrincipalsFoundException.class)
    public ResponseEntity<Object> handleNoPrincipalsFoundException(NoPrincipalsFoundException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(AuthMethodNotFoundException.class)
    public ResponseEntity<Object> handleAuthMethodNotFound(AuthMethodNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(LastAuthMethodException.class)
    public ResponseEntity<?> handleLastAuthMethodException(LastAuthMethodException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, ex.getMessage()));
    }
}
