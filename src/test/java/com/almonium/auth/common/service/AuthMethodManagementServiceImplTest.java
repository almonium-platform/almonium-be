package com.almonium.auth.common.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.repository.PrincipalRepository;
import com.almonium.auth.common.service.impl.AuthMethodManagementServiceImpl;
import com.almonium.auth.local.repository.LocalPrincipalRepository;
import com.almonium.auth.local.service.impl.PasswordEncoderService;
import com.almonium.user.core.service.UserService;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class AuthMethodManagementServiceImplTest {
    @InjectMocks
    AuthMethodManagementServiceImpl authService;

    @Mock
    VerificationTokenManagementService verificationTokenManagementService;

    @Mock
    UserService userService;

    @Mock
    PrincipalRepository principalRepository;

    @Mock
    SensitiveAuthActionService sensitiveAuthActionService;

    @Mock
    LocalPrincipalRepository localPrincipalRepository;

    @Mock
    PasswordEncoderService passwordEncoderService;
    //
    //    @DisplayName("Should verify email successfully")
    //    @Test
    //    void givenValidToken_whenVerifyEmail_thenSetVerifiedAndDeleteToken() {
    //        // Arrange
    //        String token = "validToken";
    //        LocalPrincipal principal = TestDataGenerator.buildTestLocalPrincipal();
    //        VerificationToken verificationToken = new VerificationToken(principal, token,
    // TokenType.EMAIL_VERIFICATION, 60);
    //        when(verificationTokenManagementService.getValidTokenOrThrow(token, TokenType.EMAIL_VERIFICATION))
    //                .thenReturn(verificationToken);
    //
    //        // Act
    //        authService.verifyEmail(token);
    //
    //        // Assert
    //        assertThat(principal.isEmailVerified()).isTrue();
    //        verify(localPrincipalRepository).save(principal);
    //        verify(verificationTokenManagementService).deleteToken(verificationToken);
    //    }

    //    @DisplayName("Should reset password successfully")
    //    @Test
    //    void givenValidTokenAndNewPassword_whenResetPassword_thenUpdatePasswordAndDeleteToken() {
    //        // Arrange
    //        String token = "validToken";
    //        String newPassword = "newPassword123";
    //        String encodedPassword = "2b$encodedPassword";
    //        LocalPrincipal principal = TestDataGenerator.buildTestLocalPrincipal();
    //        VerificationToken verificationToken = new VerificationToken(principal, token, TokenType.PASSWORD_RESET,
    // 60);
    //        when(passwordEncoderService.encodePassword(newPassword)).thenReturn(encodedPassword);
    //        when(verificationTokenManagementService.getValidTokenOrThrow(token, TokenType.PASSWORD_RESET))
    //                .thenReturn(verificationToken);
    //        // Act
    //        authService.resetPassword(token, newPassword);
    //
    //        // Assert
    //        assertThat(principal.getPassword()).isEqualTo(encodedPassword);
    //        verify(localPrincipalRepository).save(principal);
    //        verify(verificationTokenManagementService).deleteToken(verificationToken);
    //    }
}
