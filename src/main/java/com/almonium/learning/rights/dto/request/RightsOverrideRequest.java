package com.almonium.learning.rights.dto.request;

import com.almonium.learning.rights.model.enums.RightsEffect;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;

/** /ops blocking or allowing one work in one country, or everywhere with {@code *}; optionally answering a report. */
public record RightsOverrideRequest(
        @NotBlank @Size(max = 180) String workSlug,
        @NotBlank @Pattern(regexp = "^\\*$|^[A-Za-z]{2}$") String country,
        @NotNull RightsEffect effect,
        @NotBlank @Size(max = 1000) String reason,
        UUID reportId) {}
