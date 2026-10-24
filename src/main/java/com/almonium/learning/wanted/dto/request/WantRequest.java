package com.almonium.learning.wanted.dto.request;

import com.almonium.analyzer.translator.model.enums.Language;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

/** "I want this": a catalogue work, in the language the reader would read it in. */
public record WantRequest(
        @NotNull UUID catalogWorkId,
        @NotNull Language language,
        @Size(max = 1000) String sourceDownloadUrl) {
    public WantRequest(UUID catalogWorkId, Language language) {
        this(catalogWorkId, language, null);
    }
}
