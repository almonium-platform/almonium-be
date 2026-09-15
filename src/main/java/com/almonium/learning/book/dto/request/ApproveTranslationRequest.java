package com.almonium.learning.book.dto.request;

import jakarta.validation.constraints.Pattern;

/** Both fields optional; the defaults are the cheap, patient path. */
public record ApproveTranslationRequest(
        @Pattern(regexp = "draft|quality") String tier,
        @Pattern(regexp = "batch|inline") String mode) {
    public String tierOrDefault() {
        return tier == null ? "quality" : tier;
    }

    public String modeOrDefault() {
        return mode == null ? "batch" : mode;
    }
}
