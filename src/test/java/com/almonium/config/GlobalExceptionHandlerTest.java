package com.almonium.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.auth.local.exception.UserAlreadyExistsException;
import com.almonium.util.dto.ApiResponse;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@SuppressWarnings("DataFlowIssue")
class GlobalExceptionHandlerTest {

    GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @DisplayName("Should handle BadCredentialsException")
    @Test
    void givenBadCredentialsException_whenHandleException_thenRespondWithUnauthorized() {
        BadCredentialsException ex = new BadCredentialsException("Bad credentials");

        ResponseEntity<?> response = exceptionHandler.handleBadCredentialsException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(((ApiResponse) response.getBody()).message()).isEqualTo(ex.getMessage());
        assertThat(((ApiResponse) response.getBody()).success()).isFalse();
    }

    @DisplayName("Should handle NoResourceFoundException")
    @Test
    void givenNoResourceFoundException_whenHandleException_thenRespondWithNotFound() {
        // Arrange
        HttpMethod method = HttpMethod.GET;
        String resourcePath = "/non-existent-resource";
        NoResourceFoundException ex = new NoResourceFoundException(method, resourcePath);

        ResponseEntity<?> response = exceptionHandler.handleNoResourceFoundException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @DisplayName("Should handle UserAlreadyExistsAuthenticationException")
    @Test
    void givenUserAlreadyExistsException_whenHandleException_thenRespondWithBadRequest() {
        UserAlreadyExistsException ex = new UserAlreadyExistsException("User already exists");

        ResponseEntity<?> response = exceptionHandler.handleUserAlreadyExistsException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(((ApiResponse) response.getBody()).message()).isEqualTo(ex.getMessage());
        assertThat(((ApiResponse) response.getBody()).success()).isFalse();
    }

    @SuppressWarnings("unchecked")
    @DisplayName("Should handle MethodArgumentNotValidException")
    @Test
    void givenMethodArgumentNotValidException_whenHandleException_thenRespondWithBadRequest() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "objectName");
        bindingResult.addError(new FieldError("objectName", "field", "defaultMessage"));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Object> response = exceptionHandler.handleMethodArgumentNotValid(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Map<String, String> errors = (Map<String, String>) response.getBody();
        assertThat(errors).containsEntry("field", "defaultMessage");
    }

    @DisplayName("Should handle HttpRequestMethodNotSupportedException")
    @Test
    void givenHttpRequestMethodNotSupportedException_whenHandleException_thenRespondWithMethodNotAllowed() {
        HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException("POST");

        ResponseEntity<?> response = exceptionHandler.handleHttpRequestMethodNotSupportedException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
        assertThat(((ApiResponse) response.getBody()).message()).isEqualTo(ex.getMessage());
        assertThat(((ApiResponse) response.getBody()).success()).isFalse();
    }

    @DisplayName("Should handle general Exception")
    @Test
    void givenException_whenHandleException_thenRespondWithInternalServerError() {
        Exception ex = new Exception("Internal server error");

        ResponseEntity<?> response = exceptionHandler.handleGlobalException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(((ApiResponse) response.getBody()).message()).isEqualTo(ex.getMessage());
        assertThat(((ApiResponse) response.getBody()).success()).isFalse();
    }
}
