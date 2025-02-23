package com.almonium.infra.email.service;

import com.almonium.config.properties.AppProperties;
import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.infra.email.model.dto.EmailSubjectTemplate;
import com.almonium.subscription.model.entity.PlanSubscription;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class SubscriptionEmailComposerService extends EmailComposerService<PlanSubscription.Event> {
    public static final String PLAN_NAME = "planName";

    private static final Map<PlanSubscription.Event, EmailSubjectTemplate> TYPE_EMAIL_SUBJECT_TEMPLATE_MAP = Map.of(
            PlanSubscription.Event.CREATED,
            new EmailSubjectTemplate("Welcome to Premium!", "created"),
            PlanSubscription.Event.CANCELED,
            new EmailSubjectTemplate("Subscription Cancelled", "cancelled"),
            PlanSubscription.Event.ENDED,
            new EmailSubjectTemplate("Your Subscription Has Ended", "ended"),
            PlanSubscription.Event.RENEWED,
            new EmailSubjectTemplate("Welcome Back!", "renewed"),
            PlanSubscription.Event.PAYMENT_FAILED,
            new EmailSubjectTemplate("Payment Failed", "payment-failed"));

    private static final String BUTTON_URL_PLACEHOLDER = "url";
    private static final String SUBFOLDER = "subscription";

    // RequiredArgsConstructor not possible due to inheritance
    public SubscriptionEmailComposerService(
            SpringTemplateEngine templateEngine, AppProperties appProperties, EmailService emailService) {
        super(templateEngine, appProperties, emailService);
    }

    @Override
    public Map<PlanSubscription.Event, EmailSubjectTemplate> getTemplateTypeConfigMap() {
        return TYPE_EMAIL_SUBJECT_TEMPLATE_MAP;
    }

    @Override
    public Map<String, String> getCustomPlaceholders(EmailContext<PlanSubscription.Event> emailContext) {
        String planName = emailContext.getValue(PLAN_NAME);
        return Map.of(PLAN_NAME, planName, BUTTON_URL_PLACEHOLDER, getButtonUrl(emailContext.templateType()));
    }

    @Override
    public String getSubfolder() {
        return SUBFOLDER;
    }

    private String getButtonUrl(PlanSubscription.Event event) {
        String url =
                switch (event) {
                    case CREATED, RENEWED -> "/home";
                    case CANCELED, PAYMENT_FAILED -> "/settings/me?portal=to";
                    case ENDED -> "/pricing";
                };
        return buildActionUrl(url);
    }
}
