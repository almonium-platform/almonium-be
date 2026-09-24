package com.almonium.infra.email.service;

import com.almonium.config.properties.AppProperties;
import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.infra.email.model.dto.EmailSubjectTemplate;
import com.almonium.infra.email.model.enums.AuthEmailTemplateType;
import com.almonium.infra.email.util.PlainTextEmailBody;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class AuthEmailComposerService extends EmailComposerService<AuthEmailTemplateType> {
    public static final String ACTION_URL = "url";
    public static final String DISPLAY_ACTION_URL = "displayUrl";

    /** Account-deleted only: the name the account went by, the moment it went, and the plan that was cancelled. */
    public static final String USERNAME = "username";

    public static final String DELETED_AT = "deletedAt";
    public static final String PLAN_NAME = "planName";
    public static final String RETENTION_URL = "retentionUrl";

    private static final String RETENTION_PATH = "/delete-account#kept";

    private static final DateTimeFormatter DELETED_AT_FORMAT = DateTimeFormatter.ofPattern(
                    "d MMMM yyyy 'at' HH:mm 'UTC'", Locale.ENGLISH)
            .withZone(ZoneOffset.UTC);

    private static final Map<AuthEmailTemplateType, EmailSubjectTemplate> TEMPLATES = Map.of(
            AuthEmailTemplateType.EMAIL_VERIFICATION,
            new EmailSubjectTemplate(
                    "Verify your email address",
                    "Confirm this address to finish setting up your account.",
                    "email-verification"),
            AuthEmailTemplateType.PASSWORD_RESET,
            new EmailSubjectTemplate(
                    "Reset your password",
                    "If you didn't ask for this, ignore it — nothing changes.",
                    "password-reset"),
            AuthEmailTemplateType.EMAIL_CHANGE,
            new EmailSubjectTemplate(
                    "Confirm your email change",
                    "Your current sign-in keeps working until you confirm.",
                    "email-change"),
            AuthEmailTemplateType.ACCOUNT_DELETED,
            new EmailSubjectTemplate(
                    "Your Almonium account is deleted",
                    "Everything in it is gone, and any subscription is cancelled.",
                    "account-deleted"));

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
        if (emailContext.templateType() == AuthEmailTemplateType.ACCOUNT_DELETED) {
            return accountDeletedPlaceholders(emailContext);
        }
        String actionUrl = emailContext.attributes().get(ACTION_URL);
        return Map.of(ACTION_URL, actionUrl, DISPLAY_ACTION_URL, PlainTextEmailBody.withoutHttpsScheme(actionUrl));
    }

    /**
     * The only send with nothing to click: the account is gone, so there is no action left. The plan is optional and
     * the template hides its panel when it is absent.
     */
    private Map<String, String> accountDeletedPlaceholders(EmailContext<AuthEmailTemplateType> emailContext) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put(USERNAME, emailContext.getValue(USERNAME));
        placeholders.put(DELETED_AT, DELETED_AT_FORMAT.format(Instant.parse(emailContext.getValue(DELETED_AT))));
        placeholders.put(RETENTION_URL, buildActionUrl(RETENTION_PATH));
        String planName = emailContext.getValue(PLAN_NAME);
        if (planName != null) {
            placeholders.put(PLAN_NAME, planName);
        }
        return placeholders;
    }

    @Override
    public String getSubfolder() {
        return "auth";
    }
}
