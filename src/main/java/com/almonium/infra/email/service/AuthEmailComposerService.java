package com.almonium.infra.email.service;

import com.almonium.config.properties.AppProperties;
import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.infra.email.model.dto.EmailSubjectTemplate;
import com.almonium.infra.email.model.enums.AuthEmailTemplateType;
import com.almonium.infra.email.util.PlainTextEmailBody;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class AuthEmailComposerService extends EmailComposerService<AuthEmailTemplateType> {
    public static final String ACTION_URL = "url";
    public static final String DISPLAY_ACTION_URL = "displayUrl";

    private static final Map<AuthEmailTemplateType, EmailSubjectTemplate> TEMPLATES = Map.of(
            AuthEmailTemplateType.EMAIL_VERIFICATION,
            new EmailSubjectTemplate("Verify your email address", "email-verification"),
            AuthEmailTemplateType.PASSWORD_RESET,
            new EmailSubjectTemplate("Reset your password", "password-reset"),
            AuthEmailTemplateType.EMAIL_CHANGE,
            new EmailSubjectTemplate("Confirm your email change", "email-change"));

    public AuthEmailComposerService(
            EmailService emailService, SpringTemplateEngine templateEngine, AppProperties appProperties) {
        super(emailService, templateEngine, appProperties);
    }

    @Override
    public Map<AuthEmailTemplateType, EmailSubjectTemplate> getTemplateTypeConfigMap() {
        return TEMPLATES;
    }

    @Override
    public Map<String, String> getCustomPlaceholders(EmailContext<AuthEmailTemplateType> emailContext) {
        String actionUrl = emailContext.attributes().get(ACTION_URL);
        return Map.of(ACTION_URL, actionUrl, DISPLAY_ACTION_URL, PlainTextEmailBody.withoutHttpsScheme(actionUrl));
    }

    @Override
    public String getSubfolder() {
        return "auth";
    }
}
