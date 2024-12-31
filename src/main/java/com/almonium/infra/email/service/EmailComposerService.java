package com.almonium.infra.email.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.config.properties.AppProperties;
import com.almonium.infra.email.dto.EmailDto;
import com.almonium.infra.email.exception.EmailConfigurationException;
import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.infra.email.model.dto.EmailSubjectTemplate;
import com.almonium.infra.email.util.CssInliner;
import java.time.Year;
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
    private static final String TEMPLATE_PATH_FORMAT = "%s/%s.html";
    SpringTemplateEngine templateEngine;
    AppProperties appProperties;

    public EmailDto composeEmail(String recipientEmail, EmailContext<T> emailContext) {
        Context context = new Context();
        T templateType = emailContext.templateType();
        getCustomPlaceholders(emailContext).forEach(context::setVariable);
        buildUniversalPlaceholders().forEach(context::setVariable);

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
        return appProperties.getWebDomain() + path;
    }

    private Map<String, String> buildUniversalPlaceholders() {
        return Map.of(
                "footerText",
                String.format("© %d %s. All rights reserved.", Year.now().getValue(), appProperties.getName()),
                "headerText",
                appProperties.getName());
    }
}
