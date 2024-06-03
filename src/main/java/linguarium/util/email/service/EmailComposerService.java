package linguarium.util.email.service;

import linguarium.auth.local.model.enums.TokenType;
import linguarium.util.email.dto.EmailDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailComposerService {
    private static final String EMAIL_VERIFICATION_SUBJECT = "Verify your email address";
    private static final String EMAIL_VERIFICATION_BODY = "Please verify your email by clicking the following link: %s";
    private static final String PASSWORD_RESET_SUBJECT = "Reset your password";
    private static final String PASSWORD_RESET_BODY = "To reset your password, click the following link: %s";

    @Value("${app.server.domain}")
    private String domain;

    public EmailDto composeEmail(String recipientEmail, String token, TokenType tokenType) {
        String url = generateUrl(token, tokenType);
        String body = String.format(getBodyTemplate(tokenType), url);
        String subject = getSubject(tokenType);
        return new EmailDto(recipientEmail, subject, body);
    }

    private String getSubject(TokenType tokenType) {
        return switch (tokenType) {
            case EMAIL_VERIFICATION -> EMAIL_VERIFICATION_SUBJECT;
            case PASSWORD_RESET -> PASSWORD_RESET_SUBJECT;
        };
    }

    private String getBodyTemplate(TokenType tokenType) {
        return switch (tokenType) {
            case EMAIL_VERIFICATION -> EMAIL_VERIFICATION_BODY;
            case PASSWORD_RESET -> PASSWORD_RESET_BODY;
        };
    }

    private String generateUrl(String token, TokenType tokenType) {
        String endpoint = switch (tokenType) {
            case EMAIL_VERIFICATION -> "/verify-email";
            case PASSWORD_RESET -> "/reset-password";
        };
        return domain + endpoint + "?token=" + token;
    }
}
