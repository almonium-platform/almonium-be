package com.almonium.config;

import com.almonium.auth.common.exception.AuthMethodNotFoundException;
import com.almonium.auth.common.exception.BadAuthActionRequest;
import com.almonium.auth.common.exception.LastAuthMethodException;
import com.almonium.auth.common.exception.RecentLoginRequiredException;
import com.almonium.auth.local.exception.EmailMismatchException;
import com.almonium.auth.local.exception.EmailNotFoundException;
import com.almonium.auth.local.exception.EmailNotVerifiedException;
import com.almonium.auth.local.exception.InvalidVerificationTokenException;
import com.almonium.auth.local.exception.ReauthException;
import com.almonium.auth.local.exception.UserAlreadyExistsException;
import com.almonium.infra.email.exception.EmailConfigurationException;
import com.almonium.infra.qr.exception.QRCodeGenerationException;
import com.almonium.subscription.exception.PlanSubscriptionException;
import com.almonium.subscription.exception.PlanValidationException;
import com.almonium.subscription.exception.StripeIntegrationException;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.exception.FirebaseIntegrationException;
import com.almonium.user.core.exception.NoPrincipalFoundException;
import com.almonium.user.core.exception.ResourceConflictException;
import com.almonium.user.core.exception.ResourceNotAccessibleException;
import com.almonium.user.core.exception.StreamIntegrationException;
import com.almonium.user.friendship.exception.FriendshipNotAllowedException;
import com.almonium.util.dto.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    // // spring exceptions
    // auth exceptions
    @ExceptionHandler(RecentLoginRequiredException.class)
    public ResponseEntity<ApiResponse> handleRecentLoginRequiredException(RecentLoginRequiredException ex) {
        ApiResponse response = new ApiResponse(false, ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler({InternalAuthenticationServiceException.class})
    public ResponseEntity<ApiResponse> handleInternalAuthenticationServiceException(
            InternalAuthenticationServiceException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler({BadAuthActionRequest.class})
    public ResponseEntity<ApiResponse> handleBadAuthActionRequest(BadAuthActionRequest ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler({BadCredentialsException.class, IllegalAccessException.class})
    public ResponseEntity<ApiResponse> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, ex.getMessage()));
    }

    // controller exceptions
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorMessage = "Invalid input provided. Please check your request.";

        // Extract specific details (optional)
        Throwable rootCause = ex.getRootCause();
        if (rootCause != null) {
            String detailedMessage = rootCause.getMessage();
            if (detailedMessage.contains("Cannot deserialize value of type")) {
                String invalidValue = extractInvalidValue(detailedMessage);
                String expectedValues = extractExpectedValues(detailedMessage);
                errorMessage = String.format(
                        "Invalid value '%s'. Please use one of the accepted values: %s.", invalidValue, expectedValues);
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, errorMessage));
    }

    private String extractInvalidValue(String message) {
        try {
            return message.split("from String \"")[1].split("\"")[0];
        } catch (Exception e) {
            return "unknown value";
        }
    }

    private String extractExpectedValues(String message) {
        try {
            return message.split("accepted for Enum class: \\[")[1].split("]")[0];
        } catch (Exception e) {
            return "unknown values";
        }
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

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        String requiredType =
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "Unknown";
        String errorMessage =
                String.format("Failed to convert value '%s' to required type '%s'.", ex.getValue(), requiredType);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, errorMessage));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse> handleNoResourceFoundException(NoResourceFoundException ignored) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalException(Exception ex) {
        log.error("An error occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Something went wrong"));
    }

    // // subscription exceptions
    @ExceptionHandler(StripeIntegrationException.class)
    public ResponseEntity<ApiResponse> handleStripeIntegrationException(StripeIntegrationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(PlanSubscriptionException.class)
    public ResponseEntity<ApiResponse> handlePlanSubscriptionException(PlanSubscriptionException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(PlanValidationException.class)
    public ResponseEntity<ApiResponse> handlePlanValidationException(PlanValidationException ex) {
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(new ApiResponse(false, ex.getMessage())); // 402
    }

    // basic exceptions
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, ex.getMessage()));
    }

    // // other custom exceptions
    @ExceptionHandler
    public ResponseEntity<ApiResponse> handleQRCodeGenerationException(QRCodeGenerationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(FriendshipNotAllowedException.class)
    public ResponseEntity<ApiResponse> handleFriendshipNotAllowedException(FriendshipNotAllowedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(EmailConfigurationException.class)
    public ResponseEntity<ApiResponse> handleEmailConfigurationException(EmailConfigurationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(FirebaseIntegrationException.class)
    public ResponseEntity<ApiResponse> handleFirebaseIntegrationException(FirebaseIntegrationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(StreamIntegrationException.class)
    public ResponseEntity<ApiResponse> handleStreamIntegrationException(StreamIntegrationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotAccessibleException.class)
    public ResponseEntity<ApiResponse> handleResourceNotAccessibleException(ResourceNotAccessibleException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(BadUserRequestActionException.class)
    public ResponseEntity<ApiResponse> handleBadUserRequestActionException(BadUserRequestActionException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ApiResponse> handleResourceConflictException(ResourceConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, ex.getMessage()));
    }

    // auth
    @ExceptionHandler(ReauthException.class)
    public ResponseEntity<ApiResponse> handleReauthException(ReauthException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<ApiResponse> handleEmailNotVerifiedException(EmailNotVerifiedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(InvalidVerificationTokenException.class)
    public ResponseEntity<ApiResponse> handleInvalidTokenException(InvalidVerificationTokenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ApiResponse> handleEmailNotFoundException(EmailNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(EmailMismatchException.class)
    public ResponseEntity<Object> handleEmailMismatchException(EmailMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(NoPrincipalFoundException.class)
    public ResponseEntity<Object> handleNoPrincipalsFoundException(NoPrincipalFoundException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(AuthMethodNotFoundException.class)
    public ResponseEntity<Object> handleAuthMethodNotFound(AuthMethodNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(LastAuthMethodException.class)
    public ResponseEntity<ApiResponse> handleLastAuthMethodException(LastAuthMethodException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, ex.getMessage()));
    }
}
