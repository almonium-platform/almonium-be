package com.almonium.infra.email.model.dto;

/**
 * The inbox row of one send. The subject states the fact; the preheader states the consequence or the action and is
 * rendered as a hidden first node of the body so mail clients preview it instead of the first visible line.
 */
public record EmailSubjectTemplate(String subject, String preheader, String template) {}
