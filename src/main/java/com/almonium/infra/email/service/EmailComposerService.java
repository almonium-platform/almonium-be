package com.almonium.infra.email.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.config.properties.AppProperties;
import com.almonium.infra.email.dto.EmailDto;
import com.almonium.infra.email.exception.EmailConfigurationException;
import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.infra.email.model.dto.EmailSubjectTemplate;
import com.almonium.infra.email.util.CssInliner;
import com.almonium.infra.email.util.PlainTextEmailBody;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public abstract class EmailComposerService<T> {
    public static final String PREHEADER = "preheader";
    public static final String UNSUBSCRIBE = "unsubscribe";
    public static final String UNSUBSCRIBE_URL = "unsubscribeUrl";
    public static final String SETTINGS_URL = "settingsUrl";
    public static final String RECIPIENT_EMAIL = "recipientEmail";

    private static final String TEMPLATE_PATH_FORMAT = "%s/%s.html";
    private static final String SETTINGS_PATH = "/settings";
    private static final String NOTIFICATION_SETTINGS_PATH = "/settings/app";

    EmailService emailService;
    SpringTemplateEngine templateEngine;

    AppProperties appProperties;

    public void sendEmail(String recipientUsername, String recipientEmail, EmailContext<T> emailContext) {
        EmailDto emailDto = composeEmail(recipientUsername, recipientEmail, emailContext);
        emailService.sendEmail(emailDto);
    }

    private EmailDto composeEmail(String recipientUsername, String recipientEmail, EmailContext<T> emailContext) {
        T templateType = emailContext.templateType();
        EmailSubjectTemplate dto = getTemplateTypeConfigMap().get(templateType);
        if (dto == null) {
            throw new EmailConfigurationException("Email template not found for type: " + templateType);
        }

        Context context = new Context();
        getCustomPlaceholders(emailContext).forEach(context::setVariable);
        buildUniversalPlaceholders(recipientUsername, recipientEmail).forEach(context::setVariable);
        context.setVariable(PREHEADER, dto.preheader());
        context.setVariable(UNSUBSCRIBE, includesUnsubscribe());

        String templatePath = String.format(TEMPLATE_PATH_FORMAT, getSubfolder(), dto.template());
        String body = templateEngine.process(templatePath, context);
        body = CssInliner.inlineCss(body);
        return new EmailDto(recipientEmail, buildSubject(dto, emailContext), body, PlainTextEmailBody.fromHtml(body));
    }

    public abstract Map<T, EmailSubjectTemplate> getTemplateTypeConfigMap();

    public abstract Map<String, String> getCustomPlaceholders(EmailContext<T> emailContext);

    public abstract String getSubfolder();

    /** Subjects are fixed strings unless a composer needs to name the counterpart. */
    protected String buildSubject(EmailSubjectTemplate template, EmailContext<T> emailContext) {
        return template.subject();
    }

    /**
     * Only social sends carry an unsubscribe link. A verification mail has nothing to opt out of, and billing mail is
     * not marketing; naming the link there suggests otherwise.
     */
    protected boolean includesUnsubscribe() {
        return false;
    }

    protected String buildActionUrl(String path) {
        return appProperties.getWebDomain() + path;
    }

    private Map<String, String> buildUniversalPlaceholders(String username, String recipientEmail) {
        return Map.of(
                "username",
                username,
                RECIPIENT_EMAIL,
                recipientEmail,
                "logoUrl",
                appProperties.getWebDomain() + "/email/wordmark-white.png",
                SETTINGS_URL,
                buildActionUrl(SETTINGS_PATH),
                UNSUBSCRIBE_URL,
                buildActionUrl(NOTIFICATION_SETTINGS_PATH));
    }
}
