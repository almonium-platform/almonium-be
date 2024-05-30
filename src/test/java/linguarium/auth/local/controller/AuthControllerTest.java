package linguarium.auth.local.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import linguarium.auth.local.dto.request.LocalAuthRequest;
import linguarium.auth.local.dto.response.JwtAuthResponse;
import linguarium.auth.local.exception.EmailMismatchException;
import linguarium.auth.local.exception.UserAlreadyExistsAuthenticationException;
import linguarium.auth.local.service.AuthService;
import linguarium.auth.oauth2.model.entity.Principal;
import linguarium.base.BaseControllerTest;
import linguarium.config.GlobalExceptionHandler;
import linguarium.user.core.dto.UserInfo;
import linguarium.util.TestDataGenerator;
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
    AuthService authService;

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
        when(authService.login(eq(localAuthRequest))).thenReturn(response);

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
        when(authService.login(any(LocalAuthRequest.class))).thenThrow(new BadCredentialsException("Bad credentials"));

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
        LocalAuthRequest localAuthRequest = TestDataGenerator.createLocalAuthRequest();
        UserInfo userInfo = TestDataGenerator.buildTestUserInfo();

        JwtAuthResponse response = new JwtAuthResponse("xxx.yyy.zzz", userInfo);
        when(authService.register(eq(localAuthRequest))).thenReturn(response);

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(localAuthRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @DisplayName("Should handle existing user registration attempt by returning bad request status")
    @Test
    @SneakyThrows
    void givenExistingUser_whenRegister_thenBadRequest() {
        LocalAuthRequest registrationRequest = TestDataGenerator.createLocalAuthRequest();

        doThrow(new UserAlreadyExistsAuthenticationException("User already exists"))
                .when(authService)
                .register(any(LocalAuthRequest.class));

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false));
    }

    @DisplayName("Should add local login successfully")
    @Test
    @SneakyThrows
    void givenValidLocalLoginRequest_whenAddLocalLogin_thenSuccess() {
        LocalAuthRequest localAuthRequest = TestDataGenerator.createLocalAuthRequest();
        Principal principal = TestDataGenerator.buildTestPrincipal();

        mockMvc.perform(put(BASE_URL + "/local-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(localAuthRequest))
                        .principal(principal::getEmail))
                .andExpect(status().isOk());
    }

    @DisplayName("Should handle email mismatch when adding local login")
    @Test
    @SneakyThrows
    void givenEmailMismatch_whenAddLocalLogin_thenBadRequest() {
        LocalAuthRequest localAuthRequest = TestDataGenerator.createLocalAuthRequest();
        Principal principal = TestDataGenerator.buildTestPrincipal();
        principal.getUser().setEmail("different-email@example.com");

        doThrow(new EmailMismatchException("You need to register with the email you currently use: "
                + principal.getUser().getEmail()))
                .when(authService)
                .addLocalLogin(anyLong(), eq(localAuthRequest));

        mockMvc.perform(put(BASE_URL + "/local-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(localAuthRequest))
                        .principal(principal::getEmail))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @DisplayName("Should handle existing local login when adding local login")
    @Test
    @SneakyThrows
    void givenExistingLocalLogin_whenAddLocalLogin_thenConflict() {
        LocalAuthRequest localAuthRequest = TestDataGenerator.createLocalAuthRequest();
        Principal principal = TestDataGenerator.buildTestPrincipal();

        doThrow(new UserAlreadyExistsAuthenticationException("You already have local account registered with "
                + principal.getUser().getEmail()))
                .when(authService)
                .addLocalLogin(anyLong(), eq(localAuthRequest));

        mockMvc.perform(put(BASE_URL + "/local-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(localAuthRequest))
                        .principal(principal::getEmail))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false));
    }
}
