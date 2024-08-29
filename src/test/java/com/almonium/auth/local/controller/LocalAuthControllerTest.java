package com.almonium.auth.local.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.almonium.auth.common.model.entity.Principal;
import com.almonium.auth.local.dto.request.LocalAuthRequest;
import com.almonium.auth.local.dto.request.PasswordResetConfirmRequest;
import com.almonium.auth.local.dto.response.JwtAuthResponse;
import com.almonium.auth.local.exception.EmailNotFoundException;
import com.almonium.auth.local.exception.InvalidTokenException;
import com.almonium.auth.local.exception.UserAlreadyExistsException;
import com.almonium.auth.local.service.LocalAuthService;
import com.almonium.base.BaseControllerTest;
import com.almonium.config.GlobalExceptionHandler;
import com.almonium.user.core.dto.UserInfo;
import com.almonium.util.TestDataGenerator;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;

@WebMvcTest(
        controllers = LocalAuthController.class,
        includeFilters = {
            @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GlobalExceptionHandler.class)
        })
@FieldDefaults(level = PRIVATE)
@AutoConfigureMockMvc(addFilters = false)
class LocalAuthControllerTest extends BaseControllerTest {

    @Value("${app.endpoints.verify-email}")
    String appEndpointsVerifyEmail;

    @Value("${app.endpoints.reset-password}")
    String appEndpointsResetPassword;

    private static final String BASE_URL = "/auth/public";
    private static final String LOGIN_URL = BASE_URL + "/login";
    private static final String REGISTER_URL = BASE_URL + "/register";
    private static final String FORGOT_PASSWORD_URL = BASE_URL + "/forgot-password";
    String verifyEmailUrl;
    String resetPasswordUrl;

    @PostConstruct
    void init() {
        verifyEmailUrl = BASE_URL + appEndpointsVerifyEmail;
        resetPasswordUrl = BASE_URL + appEndpointsResetPassword;
    }

    @MockBean
    LocalAuthService localAuthService;

    @BeforeEach
    void setUp() {
        Principal principal = TestDataGenerator.buildTestPrincipal();
        SecurityContextHolder.getContext().setAuthentication(TestDataGenerator.getAuthenticationToken(principal));
    }

    @DisplayName("Should authenticate user successfully")
    @Test
    @SneakyThrows
    void givenValidCredentials_whenLogin_thenReturnJwtToken() {
        LocalAuthRequest localAuthRequest = TestDataGenerator.createLocalAuthRequest();
        UserInfo userInfo = TestDataGenerator.buildTestUserInfo();

        JwtAuthResponse response = new JwtAuthResponse("xxx.yyy.zzz", userInfo);
        when(localAuthService.login(eq(localAuthRequest))).thenReturn(response);

        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(localAuthRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @DisplayName("Should handle BadCredentialsException by returning unauthorized status")
    @Test
    @SneakyThrows
    void givenInvalidCredentials_whenLogin_thenReturnsUnauthorized() {
        LocalAuthRequest localAuthRequest = TestDataGenerator.createLocalAuthRequest();
        when(localAuthService.login(any(LocalAuthRequest.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(localAuthRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }

    @DisplayName("Should register user successfully")
    @Test
    @SneakyThrows
    void givenValidSignUpRequest_whenRegister_thenSuccess() {
        LocalAuthRequest registrationRequest = TestDataGenerator.createLocalAuthRequest();

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @DisplayName("Should handle existing user registration attempt by returning bad request status")
    @Test
    @SneakyThrows
    void givenExistingUser_whenRegister_thenBadRequest() {
        LocalAuthRequest registrationRequest = TestDataGenerator.createLocalAuthRequest();

        doThrow(new UserAlreadyExistsException("User already exists"))
                .when(localAuthService)
                .register(any(LocalAuthRequest.class));

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false));
    }

    @DisplayName("Should verify email successfully")
    @Test
    @SneakyThrows
    void givenValidToken_whenVerifyEmail_thenSuccess() {
        String token = "validToken";
        mockMvc.perform(post(verifyEmailUrl).param("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Email verified successfully"));
    }

    @DisplayName("Should handle invalid token by returning forbidden status")
    @Test
    @SneakyThrows
    void givenInvalidToken_whenVerifyEmail_thenReturnsForbidden() {
        String token = "invalidToken";
        doThrow(new InvalidTokenException("Invalid verification token"))
                .when(localAuthService)
                .verifyEmail(any(String.class));

        mockMvc.perform(post(verifyEmailUrl).param("token", token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid verification token"));
    }

    @DisplayName("Should request password reset successfully")
    @Test
    @SneakyThrows
    void givenValidEmail_whenRequestPasswordReset_thenSuccess() {
        String email = "test@example.com";
        LocalAuthRequest request = new LocalAuthRequest(email, null);

        mockMvc.perform(post(FORGOT_PASSWORD_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @DisplayName("Should return not found when requesting password reset with invalid email")
    @Test
    @SneakyThrows
    void givenInvalidEmail_whenRequestPasswordReset_thenNotFound() {
        String email = "invalid@example.com";
        LocalAuthRequest request = new LocalAuthRequest(email, null);

        doThrow(new EmailNotFoundException("Invalid email"))
                .when(localAuthService)
                .requestPasswordReset(email);

        mockMvc.perform(post(FORGOT_PASSWORD_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @DisplayName("Should reset password successfully")
    @Test
    @SneakyThrows
    void givenValidTokenAndNewPassword_whenResetPassword_thenSuccess() {
        String token = "valid-token";
        String newPassword = "newPassword123";
        PasswordResetConfirmRequest request = new PasswordResetConfirmRequest(token, newPassword);

        mockMvc.perform(post(resetPasswordUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @DisplayName("Should return forbidden when resetting password with invalid token")
    @Test
    @SneakyThrows
    void givenInvalidToken_whenResetPassword_thenForbidden() {
        String token = "invalid-token";
        String newPassword = "newPassword123";
        PasswordResetConfirmRequest request = new PasswordResetConfirmRequest(token, newPassword);

        doThrow(new InvalidTokenException("Invalid verification token"))
                .when(localAuthService)
                .resetPassword(token, newPassword);

        mockMvc.perform(post(resetPasswordUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false));
    }
}
