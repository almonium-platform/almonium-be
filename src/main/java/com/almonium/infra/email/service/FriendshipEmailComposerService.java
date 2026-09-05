package com.almonium.infra.email.service;

import com.almonium.config.properties.AppProperties;
import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.infra.email.model.dto.EmailSubjectTemplate;
import com.almonium.user.relationship.model.enums.FriendshipEvent;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class FriendshipEmailComposerService extends EmailComposerService<FriendshipEvent> {
    /** Subjects name the counterpart; {@code %s} is their username. The product word is connection, never friendship. */
    private static final Map<FriendshipEvent, EmailSubjectTemplate> TYPE_EMAIL_SUBJECT_TEMPLATE_MAP = Map.of(
            FriendshipEvent.INITIATED,
            new EmailSubjectTemplate(
                    "@%s wants to connect",
                    "Accept, ignore, or hide your profile. Ignoring sends no notification.", "initiated"),
            FriendshipEvent.ACCEPTED,
            new EmailSubjectTemplate("@%s accepted your request", "Their profile is open to you now.", "accepted"));

    public static final String COUNTERPART_USERNAME = "counterpartUsername";
    private static final String BUTTON_URL_PLACEHOLDER = "url";
    private static final String SUBFOLDER = "friendship";

    // RequiredArgsConstructor not possible due to inheritance
    public FriendshipEmailComposerService(
            EmailService emailService, SpringTemplateEngine templateEngine, AppProperties appProperties) {
        super(emailService, templateEngine, appProperties);
    }

    @Override
    public Map<FriendshipEvent, EmailSubjectTemplate> getTemplateTypeConfigMap() {
        return TYPE_EMAIL_SUBJECT_TEMPLATE_MAP;
    }

    @Override
    public Map<String, String> getCustomPlaceholders(EmailContext<FriendshipEvent> emailContext) {
        return Map.of(
                BUTTON_URL_PLACEHOLDER,
                getButtonUrl(emailContext.getValue(COUNTERPART_USERNAME)),
                COUNTERPART_USERNAME,
                emailContext.getValue(COUNTERPART_USERNAME));
    }

    @Override
    protected String buildSubject(EmailSubjectTemplate template, EmailContext<FriendshipEvent> emailContext) {
        return String.format(template.subject(), emailContext.getValue(COUNTERPART_USERNAME));
    }

    @Override
    protected boolean includesUnsubscribe() {
        return true;
    }

    @Override
    public String getSubfolder() {
        return SUBFOLDER;
    }

    private String getButtonUrl(String counterpartUsername) {
        return buildActionUrl("/users/" + counterpartUsername);
    }
}
