package com.almonium.auth.local.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.mockito.ArgumentMatchers.any;
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
import com.almonium.auth.local.exception.UserAlreadyExistsException;
import com.almonium.auth.local.service.LocalAuthService;
import com.almonium.base.BaseControllerTest;
import com.almonium.config.GlobalExceptionHandler;
import com.almonium.user.core.dto.UserInfo;
import com.almonium.util.TestDataGenerator;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
    private static final String BASE_URL = "/public/auth";
    private static final String LOGIN_URL = BASE_URL + "/login";
    private static final String REGISTER_URL = BASE_URL + "/register";
    private static final String FORGOT_PASSWORD_URL = BASE_URL + "/forgot-password";
    private static final String VERIFY_EMAIL_URL = BASE_URL + "/verify-email";
    private static final String RESET_PASSWORD_URL = BASE_URL + "/reset-password";

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

        JwtAuthResponse response = new JwtAuthResponse("xxx.yyy.zzz", "aaa.bbb.ccc", userInfo);
        when(localAuthService.login(any(LocalAuthRequest.class), any(HttpServletResponse.class)))
                .thenReturn(response);

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
        when(localAuthService.login(any(LocalAuthRequest.class), any(HttpServletResponse.class)))
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
        mockMvc.perform(post(VERIFY_EMAIL_URL).param("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Email verified successfully"));
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

        mockMvc.perform(post(RESET_PASSWORD_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
