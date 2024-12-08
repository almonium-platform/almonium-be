package com.almonium.infra.email.service;

import com.almonium.infra.email.dto.EmailDto;
import com.almonium.infra.email.exception.EmailConfigurationException;
import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.infra.email.model.dto.EmailSubjectTemplate;
import com.almonium.infra.email.util.CssInliner;
import java.time.Year;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public abstract class EmailComposerService<T> {
    private static final String TEMPLATE_PATH_FORMAT = "%s/%s.html";
    private static final Map<String, String> UNIVERSAL_EMAIL_PLACEHOLDERS = Map.of(
            "footerText", "© " + Year.now().getValue() + " Almonium. All rights reserved.", "headerText", "Almonium");

    private final SpringTemplateEngine templateEngine;

    @Value("${app.web-domain}")
    private String domain;

    public EmailDto composeEmail(String recipientEmail, EmailContext<T> emailContext) {
        Context context = new Context();
        T templateType = emailContext.templateType();
        getCustomPlaceholders(emailContext).forEach(context::setVariable);
        UNIVERSAL_EMAIL_PLACEHOLDERS.forEach(context::setVariable);

        EmailSubjectTemplate dto = getTemplateTypeConfigMap().get(templateType);
        if (dto == null) {
            throw new EmailConfigurationException("Email template not found for type: " + templateType);
        }

        String templatePath = String.format(TEMPLATE_PATH_FORMAT, getSubfolder(), dto.template());
        String body = templateEngine.process(templatePath, context);
        body = CssInliner.inlineCss(body);
        return new EmailDto(recipientEmail, dto.subject(), body);
    }

    public abstract Map<T, EmailSubjectTemplate> getTemplateTypeConfigMap();

    public abstract Map<String, String> getCustomPlaceholders(EmailContext<T> emailContext);

    public abstract String getSubfolder();

    protected String buildActionUrl(String path) {
        return domain + path;
    }
}
