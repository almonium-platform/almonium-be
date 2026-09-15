package com.almonium.infra.email.dto;

public record EmailDto(String recipient, String subject, String body, String textBody) {
    public EmailDto(String recipient, String subject, String body) {
        this(recipient, subject, body, null);
    }
}
