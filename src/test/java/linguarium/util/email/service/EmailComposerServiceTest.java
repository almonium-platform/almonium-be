package linguarium.util.email.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import linguarium.auth.local.model.enums.TokenType;
import linguarium.util.email.dto.EmailDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

@ExtendWith(MockitoExtension.class)
public class EmailComposerServiceTest {

    @Value("${app.server.domain}")
    private String domain;

    @InjectMocks
    private EmailComposerService emailComposerService;

    @DisplayName("Should compose an email with the provided recipient email, token, and token type")
    @Test
    void givenRecipientEmailTokenAndTokenType_whenComposeEmailForEmailVerification_thenEmailIsComposed() {
        // Arrange
        String recipientEmail = "test@example.com";
        String token = "testToken";
        TokenType tokenType = TokenType.EMAIL_VERIFICATION;
        String expectedSubject = "Verify your email address";
        String expectedBody = "Please verify your email by clicking the following link: " + domain + "/verify-email?token=" + token;

        // Act
        EmailDto result = emailComposerService.composeEmail(recipientEmail, token, tokenType);

        // Assert
        assertEquals(recipientEmail, result.recipient());
        assertEquals(expectedSubject, result.subject());
        assertEquals(expectedBody, result.body());
    }

    @DisplayName("Should compose an email with the provided recipient email, token, and PASSWORD_RESET token type")
    @Test
    void givenRecipientEmailTokenAndTokenType_whenComposeEmailForPasswordReset_thenEmailIsComposed() {
        // Arrange
        String recipientEmail = "test@example.com";
        String token = "testToken";
        TokenType tokenType = TokenType.PASSWORD_RESET;
        String expectedSubject = "Reset your password";
        String expectedBody = "To reset your password, click the following link: " + domain + "/reset-password?token=" + token;

        // Act
        EmailDto result = emailComposerService.composeEmail(recipientEmail, token, tokenType);

        // Assert
        assertEquals(recipientEmail, result.recipient());
        assertEquals(expectedSubject, result.subject());
        assertEquals(expectedBody, result.body());
    }
}