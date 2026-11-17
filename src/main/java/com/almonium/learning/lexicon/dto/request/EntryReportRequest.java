package com.almonium.learning.lexicon.dto.request;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.lexicon.model.enums.EntryReportReason;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

/**
 * A flag on a word ({@code senseKey} null) or on one of its senses. {@code context} is the sentence the word was met
 * in and {@code book} the edition it was read in, both optional; the lexeme and the entry version are the server's
 * to attach.
 */
public record EntryReportRequest(
        @NotNull Language language,
        @NotBlank @Size(max = 200) String headword,
        UUID senseKey,
        @NotNull EntryReportReason reason,
        @Size(max = 1000) String comment,
        @Size(max = 1000) String context,
        @Size(max = 200) String book) {}
