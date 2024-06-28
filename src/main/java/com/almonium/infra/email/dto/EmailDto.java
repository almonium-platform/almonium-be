package com.almonium.infra.email.dto;

public record EmailDto(String recipient, String subject, String body) {}
