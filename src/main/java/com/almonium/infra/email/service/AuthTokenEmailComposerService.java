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
    private static final String VERIFY_EMAIL_URL = "/verify-email";
    private static final String RESET_PASSWORD_URL = "/reset-password";

    @Value("${app.web-domain}")
    private String domain;

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
                    case EMAIL_VERIFICATION -> VERIFY_EMAIL_URL;
                    case PASSWORD_RESET -> RESET_PASSWORD_URL;
                };
        return domain + url + "?token=" + token;
    }
}
