package com.almonium.infra.email.service;

import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.infra.email.model.dto.EmailSubjectTemplate;
import com.almonium.infra.email.model.enums.EmailTemplateType;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class AuthTokenEmailComposerService extends EmailComposerService {
    private static final String SUBFOLDER = "auth";

    private static final Map<EmailTemplateType, EmailSubjectTemplate> TYPE_EMAIL_SUBJECT_TEMPLATE_MAP = Map.of(
            TokenType.EMAIL_VERIFICATION,
            new EmailSubjectTemplate("Verify your email address", "email-verification"),
            TokenType.PASSWORD_RESET,
            new EmailSubjectTemplate("Reset your password", "password-reset"),
            TokenType.EMAIL_CHANGE_VERIFICATION,
            new EmailSubjectTemplate("Confirm your email change", "email-change"));

    private static final String PLACEHOLDER = "url";
    private static final String VERIFY_EMAIL_URL = "/verify-email";
    private static final String RESET_PASSWORD_URL = "/reset-password";
    private static final String CHANGE_EMAIL_URL = "/change-email";

    public AuthTokenEmailComposerService(SpringTemplateEngine templateEngine) {
        super(templateEngine);
    }

    @Override
    public Map<EmailTemplateType, EmailSubjectTemplate> getTemplateTypeConfigMap() {
        return TYPE_EMAIL_SUBJECT_TEMPLATE_MAP;
    }

    @Override
    public Map<String, String> getCustomPlaceholders(EmailTemplateType templateType, String data) {
        String url = getApiUrlForAuthIntent(data, (TokenType) templateType);
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
                    case EMAIL_CHANGE_VERIFICATION -> CHANGE_EMAIL_URL;
                };
        return domain + url + "?token=" + token;
    }
}
