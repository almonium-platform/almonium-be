package com.almonium.infra.email.service;

import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.infra.email.dto.EmailDto;
import com.almonium.subscription.model.entity.PlanSubscription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailComposerService {
    private static final String EMAIL_VERIFICATION_SUBJECT = "Verify your email address";
    private static final String PASSWORD_RESET_SUBJECT = "Reset your password";
    private static final String SUBSCRIPTION_CREATED_SUBJECT = "Subscription Created";
    private static final String SUBSCRIPTION_UPDATED_SUBJECT = "Subscription Updated";
    private static final String SUBSCRIPTION_DELETED_SUBJECT = "Subscription Deleted";
    private static final String PAYMENT_FAILED_SUBJECT = "Payment Failed";

    private static final String EMAIL_VERIFICATION_BODY =
            """
            Please verify your email by clicking the following link: %s.
            If the link doesn't work, please paste the code into the form provided.
            If you didn't create an account, you can safely ignore this email.
            """;
    private static final String PASSWORD_RESET_BODY =
            """
            To reset your password, click the following link: %s.
            If the link doesn't work, please copy and paste it into your browser.
            If you didn't request a password reset, you can safely ignore this email.
            """;
    private static final String SUBSCRIPTION_CREATED_BODY =
            """
                    Your subscription to the %s plan has been successfully created.
                    """;
    private static final String SUBSCRIPTION_UPDATED_BODY =
            """
                    Your subscription to the %s plan has been updated.
                    """;
    private static final String SUBSCRIPTION_STOPPED_BODY =
            """
                    Your subscription to the %s plan has been suspended.
                    """;
    private static final String PAYMENT_FAILED_BODY =
            """
                    Your payment for the subscription to the %s plan has failed. Please update your payment method.
                    """;

    @Value("${app.domain}")
    private String domain;

    public EmailDto composeTokenEmail(String recipientEmail, String token, TokenType tokenType) {
        String url = generateUrl(token, tokenType);
        String body = String.format(getBodyTemplate(tokenType), url);
        String subject = getSubject(tokenType);
        return new EmailDto(recipientEmail, subject, body);
    }

    public EmailDto composeSubscriptionEmail(String recipientEmail, PlanSubscription.Event event, String planName) {
        String body = String.format(getBodyTemplate(event), planName);
        String subject = getSubject(event);
        return new EmailDto(recipientEmail, subject, body);
    }

    private String getSubject(TokenType tokenType) {
        return switch (tokenType) {
            case EMAIL_VERIFICATION -> EMAIL_VERIFICATION_SUBJECT;
            case PASSWORD_RESET -> PASSWORD_RESET_SUBJECT;
        };
    }

    private String getSubject(PlanSubscription.Event event) {
        return switch (event) {
            case SUBSCRIPTION_CREATED -> SUBSCRIPTION_CREATED_SUBJECT;
            case SUBSCRIPTION_UPDATED -> SUBSCRIPTION_UPDATED_SUBJECT;
            case SUBSCRIPTION_CANCELED -> SUBSCRIPTION_DELETED_SUBJECT;
            case PAYMENT_FAILED -> PAYMENT_FAILED_SUBJECT;
        };
    }

    private String getBodyTemplate(TokenType tokenType) {
        return switch (tokenType) {
            case EMAIL_VERIFICATION -> EMAIL_VERIFICATION_BODY;
            case PASSWORD_RESET -> PASSWORD_RESET_BODY;
        };
    }

    private String getBodyTemplate(PlanSubscription.Event event) {
        return switch (event) {
            case SUBSCRIPTION_CREATED -> SUBSCRIPTION_CREATED_BODY;
            case SUBSCRIPTION_UPDATED -> SUBSCRIPTION_UPDATED_BODY;
            case SUBSCRIPTION_CANCELED -> SUBSCRIPTION_STOPPED_BODY;
            case PAYMENT_FAILED -> PAYMENT_FAILED_BODY;
        };
    }

    private String generateUrl(String token, TokenType tokenType) {
        String endpoint =
                switch (tokenType) {
                    case EMAIL_VERIFICATION -> "/verify-email";
                    case PASSWORD_RESET -> "/reset-password";
                };
        return domain + endpoint + "?token=" + token;
    }
}
