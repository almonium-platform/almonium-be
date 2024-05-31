package linguarium.auth.common.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import linguarium.auth.common.enums.AuthProviderType;
import linguarium.auth.common.exception.AuthMethodNotFoundException;
import linguarium.auth.common.model.entity.Principal;
import linguarium.auth.common.service.AuthManagementService;
import linguarium.auth.local.dto.request.LocalAuthRequest;
import linguarium.auth.local.exception.EmailMismatchException;
import linguarium.auth.local.exception.UserAlreadyExistsAuthenticationException;
import linguarium.base.BaseControllerTest;
import linguarium.config.GlobalExceptionHandler;
import linguarium.user.core.model.entity.User;
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
import org.springframework.security.core.context.SecurityContextHolder;

@WebMvcTest(
        controllers = AuthManagementController.class,
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GlobalExceptionHandler.class)
        })
@FieldDefaults(level = PRIVATE)
@AutoConfigureMockMvc(addFilters = false)
class AuthManagementControllerTest extends BaseControllerTest {
    private static final String BASE_URL = "/auth/manage";

    @MockBean
    AuthManagementService authManagementService;

    User user;

    @BeforeEach
    void setUp() {
        Principal principal = TestDataGenerator.buildTestPrincipal();
        user = principal.getUser();
        SecurityContextHolder.getContext().setAuthentication(TestDataGenerator.getAuthenticationToken(principal));
    }

    @DisplayName("Should add local login successfully")
    @Test
    @SneakyThrows
    void givenValidLocalLoginRequest_whenAddLocalLogin_thenSuccess() {
        LocalAuthRequest localAuthRequest = TestDataGenerator.createLocalAuthRequest();
        Principal principal = TestDataGenerator.buildTestPrincipal();

        mockMvc.perform(put(BASE_URL + "/local")
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
                .when(authManagementService)
                .linkLocalAuth(anyLong(), eq(localAuthRequest));

        mockMvc.perform(put(BASE_URL + "/local")
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
                .when(authManagementService)
                .linkLocalAuth(anyLong(), eq(localAuthRequest));

        mockMvc.perform(put(BASE_URL + "/local")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(localAuthRequest))
                        .principal(principal::getEmail))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false));
    }

    @DisplayName("Should unlink provider successfully")
    @Test
    void givenValidProvider_whenUnlinkProvider_thenSuccess() throws Exception {
        // Arrange
        AuthProviderType providerType = AuthProviderType.GOOGLE;
        when(userService.getUserWithPrincipals(user.getId())).thenReturn(user);

        // Act & Assert
        mockMvc.perform(delete(BASE_URL + "/provider/" + providerType).principal(() -> "user@example.com"))
                .andExpect(status().isOk());

        verify(authManagementService).unlinkProviderAuth(user.getId(), providerType);
    }

    @DisplayName("Should return 404 when provider not found")
    @Test
    void givenInvalidProvider_whenUnlinkProvider_thenNotFound() throws Exception {
        // Arrange
        AuthProviderType providerType = AuthProviderType.GOOGLE;
        doThrow(new AuthMethodNotFoundException("Auth method not found " + providerType))
                .when(authManagementService)
                .unlinkProviderAuth(user.getId(), providerType);

        // Act & Assert
        mockMvc.perform(delete(BASE_URL + "/provider/" + providerType).principal(() -> "user@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Auth method not found " + providerType));

        verify(authManagementService).unlinkProviderAuth(user.getId(), providerType);
    }
}
