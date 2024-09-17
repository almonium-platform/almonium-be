package com.almonium.auth.common.controller;

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

import com.almonium.auth.common.exception.AuthMethodNotFoundException;
import com.almonium.auth.common.model.entity.Principal;
import com.almonium.auth.common.model.enums.AuthProviderType;
import com.almonium.auth.common.service.AuthManagementService;
import com.almonium.auth.local.dto.request.LocalAuthRequest;
import com.almonium.auth.local.exception.EmailMismatchException;
import com.almonium.auth.local.exception.UserAlreadyExistsException;
import com.almonium.base.BaseControllerTest;
import com.almonium.config.GlobalExceptionHandler;
import com.almonium.user.core.model.entity.User;
import com.almonium.util.TestDataGenerator;
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
    private static final String BASE_URL = "/auth";
    private static final String PROVIDERS_URL = BASE_URL + "/providers/";

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

        doThrow(new UserAlreadyExistsException("You already have local account registered with "
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
        mockMvc.perform(delete(PROVIDERS_URL + providerType).principal(() -> "user@example.com"))
                .andExpect(status().isOk());

        verify(authManagementService).unlinkAuthMethod(user.getId(), providerType);
    }

    @DisplayName("Should return 404 when provider not found")
    @Test
    void givenInvalidProvider_whenUnlinkProvider_thenNotFound() throws Exception {
        // Arrange
        AuthProviderType providerType = AuthProviderType.GOOGLE;
        doThrow(new AuthMethodNotFoundException("Auth method not found " + providerType))
                .when(authManagementService)
                .unlinkAuthMethod(user.getId(), providerType);

        // Act & Assert
        mockMvc.perform(delete(PROVIDERS_URL + providerType).principal(() -> "user@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Auth method not found " + providerType));

        verify(authManagementService).unlinkAuthMethod(user.getId(), providerType);
    }
}
