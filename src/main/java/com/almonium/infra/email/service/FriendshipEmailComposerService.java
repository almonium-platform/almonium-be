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
    private static final Map<FriendshipEvent, EmailSubjectTemplate> TYPE_EMAIL_SUBJECT_TEMPLATE_MAP = Map.of(
            FriendshipEvent.INITIATED, new EmailSubjectTemplate("New friendship request", "initiated"),
            FriendshipEvent.ACCEPTED, new EmailSubjectTemplate("Friendship request accepted", "accepted"));

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
    public String getSubfolder() {
        return SUBFOLDER;
    }

    private String getButtonUrl(String counterpartUsername) {
        return buildActionUrl("/users/" + counterpartUsername);
    }
}
