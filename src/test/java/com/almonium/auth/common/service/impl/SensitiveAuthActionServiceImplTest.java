package com.almonium.auth.common.service.impl;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.auth.common.exception.AuthMethodNotFoundException;
import com.almonium.auth.common.exception.BadAuthActionRequest;
import com.almonium.auth.common.exception.LastAuthMethodException;
import com.almonium.auth.common.factory.PrincipalFactory;
import com.almonium.auth.common.model.entity.Principal;
import com.almonium.auth.common.model.enums.AuthProviderType;
import com.almonium.auth.common.repository.PrincipalRepository;
import com.almonium.auth.common.service.VerificationTokenManagementService;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.service.impl.PasswordEncoderService;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.UserService;
import com.almonium.util.TestDataGenerator;
import java.util.Optional;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class SensitiveAuthActionServiceImplTest {
    @InjectMocks
    SensitiveAuthActionServiceImpl authService;

    @Mock
    VerificationTokenManagementService verificationTokenManagementService;

    @Mock
    UserService userService;

    @Mock
    PrincipalRepository principalRepository;

    @Mock
    PrincipalFactory principalFactory;

    @Mock
    PasswordEncoderService passwordEncoderService;

    @DisplayName("Should add local login successfully")
    @Test
    void givenValidLocalLoginRequest_whenLinkLocal_thenSuccess() {
        // Arrange
        User user = TestDataGenerator.buildTestUserWithId();

        String token = "123456";
        when(userService.getUserWithPrincipals(user.getId())).thenReturn(user);
        String password = "password";
        when(principalFactory.createLocalPrincipal(user, password))
                .thenReturn(new LocalPrincipal(user, "email@mail.com", "encodedPassword"));

        // Act
        authService.linkLocal(user.getId(), password);

        // Assert
        verify(userService).getUserWithPrincipals(user.getId());
        verify(principalFactory).createLocalPrincipal(user, password);
        verify(principalRepository).save(any(Principal.class));
    }

    @DisplayName("Should throw exception when local login already exists")
    @Test
    void givenExistingLocalLogin_whenLinkLocal_thenThrowBadAuthActionRequestException() {
        // Arrange
        User user = TestDataGenerator.buildTestUserWithId();
        LocalPrincipal existingPrincipal = LocalPrincipal.builder()
                .user(user)
                .provider(AuthProviderType.LOCAL)
                .build();
        user.getPrincipals().add(existingPrincipal);

        when(userService.getUserWithPrincipals(user.getId())).thenReturn(user);
        when(userService.getLocalPrincipal(user)).thenReturn(Optional.of(existingPrincipal));
        String password = "password";

        // Act & Assert
        assertThatThrownBy(() -> authService.linkLocal(user.getId(), password))
                .isInstanceOf(BadAuthActionRequest.class)
                .hasMessageContaining("Local auth method already exists for user: " + user.getEmail());

        verify(userService).getLocalPrincipal(user);
        verify(principalRepository, never()).save(any(Principal.class));
    }

    @DisplayName("Should unlink provider successfully")
    @Test
    void givenValidProvider_whenUnlinkProvider_thenSuccess() {
        // Arrange
        User user = TestDataGenerator.buildTestUserWithId();
        Principal principalGoogle = TestDataGenerator.buildTestPrincipal(AuthProviderType.GOOGLE);
        Principal principalFacebook = TestDataGenerator.buildTestPrincipal(AuthProviderType.FACEBOOK);
        user.getPrincipals().add(principalGoogle);
        user.getPrincipals().add(principalFacebook);

        when(userService.getUserWithPrincipals(user.getId())).thenReturn(user);

        // Act
        authService.unlinkAuthMethod(user.getId(), AuthProviderType.GOOGLE);

        // Assert
        verify(userService).getUserWithPrincipals(user.getId());
        verify(principalRepository).delete(principalGoogle);
    }

    @DisplayName("Should throw exception when provider not found")
    @Test
    void givenInvalidProvider_whenUnlinkProvider_thenThrowException() {
        // Arrange
        User user = TestDataGenerator.buildTestUserWithId();

        when(userService.getUserWithPrincipals(user.getId())).thenReturn(user);

        // Act & Assert
        assertThatThrownBy(() -> authService.unlinkAuthMethod(user.getId(), AuthProviderType.GOOGLE))
                .isInstanceOf(AuthMethodNotFoundException.class)
                .hasMessageContaining("Auth method not found GOOGLE");

        verify(userService).getUserWithPrincipals(user.getId());
        verify(principalRepository, never()).delete(any(Principal.class));
    }

    @DisplayName("Should throw exception when trying to unlink the last auth method")
    @Test
    void givenLastAuthMethod_whenUnlinkProvider_thenThrowLastAuthMethodException() {
        // Arrange
        User user = TestDataGenerator.buildTestUserWithId();
        Principal principal = TestDataGenerator.buildTestPrincipal(AuthProviderType.LOCAL);
        user.getPrincipals().add(principal);

        when(userService.getUserWithPrincipals(user.getId())).thenReturn(user);

        // Act & Assert
        assertThatThrownBy(() -> authService.unlinkAuthMethod(user.getId(), AuthProviderType.LOCAL))
                .isInstanceOf(LastAuthMethodException.class)
                .hasMessageContaining("Cannot remove the last authentication method for the user: " + user.getEmail());

        verify(userService).getUserWithPrincipals(user.getId());
        verify(principalRepository, never()).delete(any(Principal.class));
    }
}
