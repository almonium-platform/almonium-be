package com.almonium.infra.email.service;

import com.almonium.config.properties.AppProperties;
import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.infra.email.model.dto.EmailSubjectTemplate;
import com.almonium.subscription.model.entity.PlanSubscription;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class SubscriptionEmailComposerService extends EmailComposerService<PlanSubscription.Event> {
    public static final String PLAN_NAME = "planName";

    /** ISO-8601 instant at which the current paid period ends; optional. */
    public static final String PERIOD_ENDS_AT = "periodEndsAt";

    /** Template-facing day, e.g. {@code 12 September 2026}; only set when the period end is still ahead. */
    public static final String PERIOD_END_DATE = "periodEndDate";

    private static final DateTimeFormatter PERIOD_END_FORMAT =
            DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH).withZone(ZoneOffset.UTC);

    private static final Map<PlanSubscription.Event, EmailSubjectTemplate> TYPE_EMAIL_SUBJECT_TEMPLATE_MAP = Map.of(
            PlanSubscription.Event.CREATED,
            new EmailSubjectTemplate(
                    "Your membership is active",
                    "Every level-adapted edition, private imports, and unlimited saved words.",
                    "created"),
            PlanSubscription.Event.CANCELED,
            new EmailSubjectTemplate(
                    "Your subscription is cancelled",
                    "Access continues to the end of the current billing period.",
                    "cancelled"),
            PlanSubscription.Event.ENDED,
            new EmailSubjectTemplate(
                    "Your subscription has ended", "Premium access is off. You can upgrade again any time.", "ended"),
            PlanSubscription.Event.RENEWED,
            new EmailSubjectTemplate(
                    "Your subscription renewed",
                    "Nothing to do. Billing and cancellation live in Settings.",
                    "renewed"),
            PlanSubscription.Event.REACTIVATED,
            new EmailSubjectTemplate(
                    "Your subscription is active again",
                    "Level-adapted editions and private imports are open again.",
                    "reactivated"),
            PlanSubscription.Event.PAYMENT_FAILED,
            new EmailSubjectTemplate(
                    "We couldn't take your payment",
                    "Premium is still on. Update your card to avoid an interruption.",
                    "payment-failed"));

    private static final String BUTTON_URL_PLACEHOLDER = "url";
    private static final String SUBFOLDER = "subscription";

    // RequiredArgsConstructor not possible due to inheritance
    public SubscriptionEmailComposerService(
            EmailService emailService, SpringTemplateEngine templateEngine, AppProperties appProperties) {
        super(emailService, templateEngine, appProperties);
    }

    @Override
    public Map<PlanSubscription.Event, EmailSubjectTemplate> getTemplateTypeConfigMap() {
        return TYPE_EMAIL_SUBJECT_TEMPLATE_MAP;
    }

    @Override
    public Map<String, String> getCustomPlaceholders(EmailContext<PlanSubscription.Event> emailContext) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put(PLAN_NAME, emailContext.getValue(PLAN_NAME));
        placeholders.put(BUTTON_URL_PLACEHOLDER, getButtonUrl(emailContext.templateType()));
        upcomingPeriodEnd(emailContext)
                .ifPresent(end -> placeholders.put(PERIOD_END_DATE, PERIOD_END_FORMAT.format(end)));
        return placeholders;
    }

    @Override
    public String getSubfolder() {
        return SUBFOLDER;
    }

    /**
     * The renewal receipt can be composed before Paddle's period update lands, in which case the stored period end is
     * the one that just passed. A date behind the reader is worse than no date, so it is left out.
     */
    private Optional<Instant> upcomingPeriodEnd(EmailContext<PlanSubscription.Event> emailContext) {
        return Optional.ofNullable(emailContext.getValue(PERIOD_ENDS_AT))
                .map(Instant::parse)
                .filter(end -> end.isAfter(Instant.now()));
    }

    private String getButtonUrl(PlanSubscription.Event event) {
        String url =
                switch (event) {
                    case CREATED, RENEWED, REACTIVATED -> "/home";
                    case CANCELED, PAYMENT_FAILED -> "/membership?portal=to";
                    case ENDED -> "/membership";
                };
        return buildActionUrl(url);
    }
}
