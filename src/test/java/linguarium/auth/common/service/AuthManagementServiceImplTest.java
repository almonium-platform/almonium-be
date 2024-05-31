package linguarium.auth.common.service;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import linguarium.auth.common.enums.AuthProviderType;
import linguarium.auth.common.exception.AuthMethodNotFoundException;
import linguarium.auth.common.model.entity.Principal;
import linguarium.auth.common.repository.PrincipalRepository;
import linguarium.auth.common.service.impl.AuthManagementServiceImpl;
import linguarium.auth.common.util.PrincipalFactory;
import linguarium.auth.local.dto.request.LocalAuthRequest;
import linguarium.auth.local.exception.EmailMismatchException;
import linguarium.auth.local.exception.UserAlreadyExistsAuthenticationException;
import linguarium.auth.local.model.entity.LocalPrincipal;
import linguarium.user.core.model.entity.User;
import linguarium.user.core.service.UserService;
import linguarium.util.TestDataGenerator;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class AuthManagementServiceImplTest {
    @InjectMocks
    AuthManagementServiceImpl authService;

    @Mock
    UserService userService;

    @Mock
    PrincipalRepository principalRepository;

    @Mock
    PrincipalFactory passwordEncoder;

    @DisplayName("Should add local login successfully")
    @Test
    void givenValidLocalLoginRequest_whenLinkLocalAuth_thenSuccess() {
        // Arrange
        LocalAuthRequest localAuthRequest = TestDataGenerator.createLocalAuthRequest();
        User user = TestDataGenerator.buildTestUser();
        user.setEmail(localAuthRequest.email()); // Ensure email matches

        when(userService.getUserWithPrincipals(user.getId())).thenReturn(user);
        when(passwordEncoder.createLocalPrincipal(user, localAuthRequest))
                .thenReturn(new LocalPrincipal(user, localAuthRequest.email(), "encodedPassword"));

        // Act
        authService.linkLocalAuth(user.getId(), localAuthRequest);

        // Assert
        verify(userService).getUserWithPrincipals(user.getId());
        verify(passwordEncoder).createLocalPrincipal(user, localAuthRequest);
        verify(principalRepository).save(any(Principal.class));
    }

    @DisplayName("Should throw exception when email mismatch on adding local login")
    @Test
    void givenEmailMismatch_whenLinkLocalAuth_thenThrowEmailMismatchException() {
        // Arrange
        LocalAuthRequest localAuthRequest = TestDataGenerator.createLocalAuthRequest();
        User user = TestDataGenerator.buildTestUser();
        user.setEmail("different-email@example.com");

        when(userService.getUserWithPrincipals(user.getId())).thenReturn(user);

        // Act & Assert
        assertThatThrownBy(() -> authService.linkLocalAuth(user.getId(), localAuthRequest))
                .isInstanceOf(EmailMismatchException.class)
                .hasMessageContaining("You need to register with the email you currently use: " + user.getEmail());

        verify(userService).getUserWithPrincipals(user.getId());
        verify(principalRepository, never()).save(any(Principal.class));
    }

    @DisplayName("Should throw exception when local login already exists")
    @Test
    void givenExistingLocalLogin_whenLinkLocalAuth_thenThrowUserAlreadyExistsAuthenticationException() {
        // Arrange
        LocalAuthRequest localAuthRequest = TestDataGenerator.createLocalAuthRequest();
        User user = TestDataGenerator.buildTestUser();
        Principal existingPrincipal = LocalPrincipal.builder()
                .user(user)
                .provider(AuthProviderType.LOCAL)
                .build();
        user.getPrincipals().add(existingPrincipal);

        when(userService.getUserWithPrincipals(user.getId())).thenReturn(user);

        // Act & Assert
        assertThatThrownBy(() -> authService.linkLocalAuth(user.getId(), localAuthRequest))
                .isInstanceOf(UserAlreadyExistsAuthenticationException.class)
                .hasMessageContaining("You already have local account registered with " + user.getEmail());

        verify(userService).getUserWithPrincipals(user.getId());
        verify(principalRepository, never()).save(any(Principal.class));
    }

    @DisplayName("Should unlink provider successfully")
    @Test
    void givenValidProvider_whenUnlinkProvider_thenSuccess() {
        // Arrange
        User user = TestDataGenerator.buildTestUser();
        Principal principal = TestDataGenerator.buildTestPrincipal(AuthProviderType.GOOGLE);
        user.getPrincipals().add(principal);

        when(userService.getUserWithPrincipals(user.getId())).thenReturn(user);

        // Act
        authService.unlinkProviderAuth(user.getId(), AuthProviderType.GOOGLE);

        // Assert
        verify(userService).getUserWithPrincipals(user.getId());
        verify(principalRepository).delete(principal);
    }

    @DisplayName("Should throw exception when provider not found")
    @Test
    void givenInvalidProvider_whenUnlinkProvider_thenThrowException() {
        // Arrange
        User user = TestDataGenerator.buildTestUser();

        when(userService.getUserWithPrincipals(user.getId())).thenReturn(user);

        // Act & Assert
        assertThatThrownBy(() -> authService.unlinkProviderAuth(user.getId(), AuthProviderType.GOOGLE))
                .isInstanceOf(AuthMethodNotFoundException.class)
                .hasMessageContaining("Auth method not found GOOGLE");

        verify(userService).getUserWithPrincipals(user.getId());
        verify(principalRepository, never()).delete(any(Principal.class));
    }
}
