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
            FriendshipEvent.INITIATED, new EmailSubjectTemplate("You've Got a New Friendship Request!", "initiated"),
            FriendshipEvent.ACCEPTED, new EmailSubjectTemplate("Your Friendship Request Was Accepted!", "accepted"));

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
                getButtonUrl(emailContext.templateType()),
                COUNTERPART_USERNAME,
                emailContext.getValue(COUNTERPART_USERNAME));
    }

    @Override
    public String getSubfolder() {
        return SUBFOLDER;
    }

    private String getButtonUrl(FriendshipEvent event) {
        String url =
                switch (event) {
                    case INITIATED -> "/social?requests=received";
                    case ACCEPTED -> "/social?tab=friends";
                };
        return buildActionUrl(url);
    }
}
