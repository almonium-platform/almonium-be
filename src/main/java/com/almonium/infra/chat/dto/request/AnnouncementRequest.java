package com.almonium.infra.chat.dto.request;

import com.almonium.analyzer.translator.model.enums.Language;
import jakarta.validation.constraints.NotBlank;

/**
 * A post to a broadcast channel: the app-wide one when no language is given, else that room's.
 *
 * <p>A post announces something, and the thing it announces travels with it as one footer action - a label and where
 * it leads. Both are optional and only count together: a label with nowhere to go is a button that does nothing.
 */
public record AnnouncementRequest(
        Language language, @NotBlank String text, String ctaLabel, String ctaUrl) {
    public boolean hasCta() {
        return ctaLabel != null && !ctaLabel.isBlank() && ctaUrl != null && !ctaUrl.isBlank();
    }
}
