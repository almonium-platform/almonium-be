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
            PlanSubscription.Event.SUBSCRIPTION_CREATED,
            new EmailSubjectTemplate("Subscription Created", "subscription-created"),
            PlanSubscription.Event.SUBSCRIPTION_CANCELED,
            new EmailSubjectTemplate("Subscription Cancelled", "subscription-cancelled"),
            PlanSubscription.Event.SUBSCRIPTION_ENDED,
            new EmailSubjectTemplate("Subscription Ended", "subscription-ended"),
            PlanSubscription.Event.SUBSCRIPTION_RENEWED,
            new EmailSubjectTemplate("Subscription Ended", "subscription-renewed"),
            PlanSubscription.Event.SUBSCRIPTION_PAYMENT_FAILED,
            new EmailSubjectTemplate("Payment Failed", "subscription-payment-failed"));

    private static final String PLACEHOLDER = "planName";

    public SubscriptionEmailComposerService(SpringTemplateEngine templateEngine) {
        super(templateEngine);
    }

    @Override
    public Map<EmailTemplateType, EmailSubjectTemplate> getTemplateTypeConfigMap() {
        return TYPE_EMAIL_SUBJECT_TEMPLATE_MAP;
    }

    @Override
    public Map<String, String> getCustomPlaceholders(EmailTemplateType templateType, String planName) {
        return Map.of(PLACEHOLDER, planName);
    }

    @Override
    public String getSubfolder() {
        return SUBFOLDER;
    }
}
