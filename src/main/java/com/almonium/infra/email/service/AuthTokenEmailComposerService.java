package com.almonium.infra.email.service;

import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.infra.email.model.dto.EmailSubjectTemplate;
import com.almonium.infra.email.model.enums.EmailTemplateType;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class AuthTokenEmailComposerService extends EmailComposerService {
    private static final String SUBFOLDER = "auth";

    private static final Map<EmailTemplateType, EmailSubjectTemplate> TYPE_EMAIL_SUBJECT_TEMPLATE_MAP = Map.of(
            TokenType.EMAIL_VERIFICATION,
            new EmailSubjectTemplate("Verify your email address", "email-verification"),
            TokenType.PASSWORD_RESET,
            new EmailSubjectTemplate("Reset your password", "password-reset"));

    private static final String PLACEHOLDER = "url";

    @Value("${app.domain}")
    private String domain;

    @Value("${app.endpoints.verify-email}")
    private String emailVerificationUrl;

    @Value("${app.endpoints.reset-password}")
    private String passwordResetUrl;

    public AuthTokenEmailComposerService(SpringTemplateEngine templateEngine) {
        super(templateEngine);
    }

    @Override
    public Map<EmailTemplateType, EmailSubjectTemplate> getTemplateTypeConfigMap() {
        return TYPE_EMAIL_SUBJECT_TEMPLATE_MAP;
    }

    @Override
    public Map<String, String> getCustomPlaceholders(EmailTemplateType templateType, String token) {
        String url = getApiUrlForAuthIntent(token, (TokenType) templateType);
        return Map.of(PLACEHOLDER, url);
    }

    @Override
    public String getSubfolder() {
        return SUBFOLDER;
    }

    private String getApiUrlForAuthIntent(String token, TokenType tokenType) {
        String url =
                switch (tokenType) {
                    case EMAIL_VERIFICATION -> emailVerificationUrl;
                    case PASSWORD_RESET -> passwordResetUrl;
                };
        return domain + url + "?token=" + token;
    }
}
