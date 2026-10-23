package com.almonium.info.feedback.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * The landing page's one question. {@code email} is optional: without it the founder reads the answer but cannot
 * reply. {@code locale} is the browser's language tag. {@code website} is a honeypot the page never shows, so only a
 * bot fills it in.
 */
public record FeedbackRequest(
        @NotBlank @Size(min = 10, max = 2000) String message,
        @Email @Size(max = 320) String email,
        @Size(max = 35) String locale,
        @Size(max = 200) String website) {}
