package com.linguarium.auth.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.linguarium.auth.dto.UserInfo;
import com.linguarium.auth.dto.request.LoginRequest;
import com.linguarium.auth.dto.request.RegisterRequest;
import com.linguarium.auth.dto.response.JwtAuthResponse;
import com.linguarium.auth.exception.UserAlreadyExistsAuthenticationException;
import com.linguarium.auth.service.LocalAuthService;
import com.linguarium.base.BaseControllerTest;
import com.linguarium.config.GlobalExceptionHandler;
import com.linguarium.util.TestDataGenerator;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;

@WebMvcTest(
        controllers = AuthController.class,
        includeFilters = {
            @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GlobalExceptionHandler.class)
        })
@FieldDefaults(level = PRIVATE)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest extends BaseControllerTest {
    private static final String BASE_URL = "/auth";
    private static final String LOGIN_URL = BASE_URL + "/login";
    private static final String REGISTER_URL = BASE_URL + "/register";

    @MockBean
    LocalAuthService localAuthService;

    @DisplayName("Should authenticate user successfully")
    @Test
    @SneakyThrows
    void givenValidCredentials_whenLogin_thenReturnJwtToken() {
        LoginRequest loginRequest = new LoginRequest("user@example.com", "password");
        UserInfo userInfo = TestDataGenerator.buildTestUserInfo();

        JwtAuthResponse response = new JwtAuthResponse("xxx.yyy.zzz", userInfo);
        when(localAuthService.login(eq(loginRequest))).thenReturn(response);

        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @DisplayName("Should handle BadCredentialsException by returning unauthorized status")
    @Test
    @SneakyThrows
    void givenInvalidCredentials_whenLogin_thenReturnsUnauthorized() {
        LoginRequest loginRequest = new LoginRequest("user@example.com", "wrong_password");
        when(localAuthService.login(any(LoginRequest.class))).thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }

    @DisplayName("Should register user successfully")
    @Test
    @SneakyThrows
    void givenValidSignUpRequest_whenRegister_thenSuccess() {
        RegisterRequest registrationRequest = createSignUpRequest();

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
        RegisterRequest registrationRequest = createSignUpRequest();

        doThrow(new UserAlreadyExistsAuthenticationException("User already exists"))
                .when(localAuthService)
                .register(any(RegisterRequest.class));

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    private RegisterRequest createSignUpRequest() {
        return RegisterRequest.builder()
                .username("dummyUsername")
                .email("dummy@example.com")
                .password("dummyPassword123")
                .build();
    }
}
