package com.almonium.infra.email.service;

import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.config.properties.AppProperties;
import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.infra.email.model.dto.EmailSubjectTemplate;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class AuthTokenEmailComposerService extends EmailComposerService<TokenType> {
    public static final String TOKEN_ATTRIBUTE = "token";

    private static final String SUBFOLDER = "auth";

    private static final Map<TokenType, EmailSubjectTemplate> TYPE_EMAIL_SUBJECT_TEMPLATE_MAP = Map.of(
            TokenType.EMAIL_VERIFICATION,
            new EmailSubjectTemplate("Verify your email address", "email-verification"),
            TokenType.PASSWORD_RESET,
            new EmailSubjectTemplate("Reset your password", "password-reset"),
            TokenType.EMAIL_CHANGE_VERIFICATION,
            new EmailSubjectTemplate("Confirm your email change", "email-change"));

    private static final String BUTTON_URL_PLACEHOLDER = "url";
    private static final String VERIFY_EMAIL_URL = "/verify-email";
    private static final String RESET_PASSWORD_URL = "/reset-password";
    private static final String CHANGE_EMAIL_URL = "/change-email";

    public AuthTokenEmailComposerService(SpringTemplateEngine templateEngine, AppProperties appProperties) {
        super(templateEngine, appProperties);
    }

    @Override
    public Map<TokenType, EmailSubjectTemplate> getTemplateTypeConfigMap() {
        return TYPE_EMAIL_SUBJECT_TEMPLATE_MAP;
    }

    @Override
    public Map<String, String> getCustomPlaceholders(EmailContext<TokenType> emailContext) {
        String token = emailContext.getValue(TOKEN_ATTRIBUTE);
        String url = getApiUrlForAuthIntent(token, emailContext.templateType());
        return Map.of(BUTTON_URL_PLACEHOLDER, url);
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
        return buildActionUrl(url + "?token=" + token);
    }
}
