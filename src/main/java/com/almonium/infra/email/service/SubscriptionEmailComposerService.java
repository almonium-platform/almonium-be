package com.almonium.infra.email.service;

import com.almonium.infra.email.model.dto.EmailSubjectTemplate;
import com.almonium.infra.email.model.enums.EmailTemplateType;
import com.almonium.subscription.model.entity.PlanSubscription;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class SubscriptionEmailComposerService extends EmailComposerService {
    private static final String SUBFOLDER = "subscription";

    private static final Map<EmailTemplateType, EmailSubjectTemplate> TYPE_EMAIL_SUBJECT_TEMPLATE_MAP = Map.of(
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

    private static final String PLAN_NAME = "planName";
    private static final String BUTTON_URL = "url";

    public SubscriptionEmailComposerService(SpringTemplateEngine templateEngine) {
        super(templateEngine);
    }

    @Override
    public Map<EmailTemplateType, EmailSubjectTemplate> getTemplateTypeConfigMap() {
        return TYPE_EMAIL_SUBJECT_TEMPLATE_MAP;
    }

    @Override
    public Map<String, String> getCustomPlaceholders(EmailTemplateType templateType, String planName) {
        return Map.of(PLAN_NAME, planName, BUTTON_URL, getButtonUrl((PlanSubscription.Event) templateType));
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
        return this.domain + url;
    }
}
