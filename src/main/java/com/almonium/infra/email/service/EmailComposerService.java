package com.almonium.infra.email.service;

import com.almonium.infra.email.dto.EmailDto;
import com.almonium.infra.email.model.dto.EmailSubjectTemplate;
import com.almonium.infra.email.model.enums.EmailTemplateType;
import jakarta.validation.constraints.NotNull;
import java.time.Year;
import java.util.Map;
import java.util.StringTokenizer;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public abstract class EmailComposerService {
    private static final String TEMPLATE_PATH_FORMAT = "%s/%s.html";
    private static final Map<String, String> UNIVERSAL_EMAIL_PLACEHOLDERS = Map.of(
            "footerText", "© " + Year.now().getValue() + " Almonium. All rights reserved.", "headerText", "Almonium");

    private final SpringTemplateEngine templateEngine;

    public EmailDto composeEmail(String recipientEmail, EmailTemplateType templateType, String tokenOrPlanName) {
        Context context = new Context();
        getCustomPlaceholders(templateType, tokenOrPlanName).forEach(context::setVariable);
        UNIVERSAL_EMAIL_PLACEHOLDERS.forEach(context::setVariable);

        EmailSubjectTemplate dto = getTemplateTypeConfigMap().get(templateType);
        String templatePath = String.format(TEMPLATE_PATH_FORMAT, getSubfolder(), dto.template());
        String body = templateEngine.process(templatePath, context);
        body = inlineCss(body);
        return new EmailDto(recipientEmail, dto.subject(), body);
    }

    private static String inlineCss(String html) {
        final String style = "style";
        Document doc = Jsoup.parse(html);
        Elements els = doc.select(style); // to get all the style elements
        for (Element e : els) {
            String styleRules = e.getAllElements()
                    .get(0)
                    .data()
                    .replaceAll("\n", StringUtils.EMPTY)
                    .trim();
            String delims = "{}";
            StringTokenizer st = new StringTokenizer(styleRules, delims);
            while (st.countTokens() > 1) {
                String selector = st.nextToken(), properties = st.nextToken();
                if (!selector.contains(":")) { // skip a:hover rules, etc.
                    Elements selectedElements = doc.select(selector);
                    for (Element selElem : selectedElements) {
                        String oldProperties = selElem.attr(style);
                        selElem.attr(
                                style,
                                !oldProperties.isEmpty()
                                        ? concatenateProperties(oldProperties, properties)
                                        : properties);
                    }
                }
            }
            e.remove();
        }
        return doc.toString();
    }

    private static String concatenateProperties(String oldProp, @NotNull String newProp) {
        oldProp = oldProp.trim();
        if (!oldProp.endsWith(";")) oldProp += ";";
        return oldProp + newProp.replaceAll("\\s{2,}", " ");
    }

    public abstract Map<EmailTemplateType, EmailSubjectTemplate> getTemplateTypeConfigMap();

    public abstract Map<String, String> getCustomPlaceholders(EmailTemplateType templateType, String tokenOrPlanName);

    public abstract String getSubfolder();
}
