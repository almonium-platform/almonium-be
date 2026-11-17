package com.almonium.learning.lexicon.dto.request;

import com.almonium.learning.lexicon.model.enums.EntryReportReason;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/** One group of the /ops queue: every open report of this reason on this sense (or on the word, without a key). */
public record EntryReportResolveRequest(
        @NotBlank String language,
        @NotBlank String headword,
        UUID senseKey,
        @NotNull EntryReportReason reason) {}
