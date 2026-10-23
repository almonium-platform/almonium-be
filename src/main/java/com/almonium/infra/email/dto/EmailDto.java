package com.almonium.infra.email.dto;

/** {@code replyTo} is set only when a reply should reach someone other than the sending domain, such as a visitor. */
public record EmailDto(String recipient, String subject, String body, String textBody, String replyTo) {
    public EmailDto(String recipient, String subject, String body, String textBody) {
        this(recipient, subject, body, textBody, null);
    }

    public EmailDto(String recipient, String subject, String body) {
        this(recipient, subject, body, null, null);
    }
}
