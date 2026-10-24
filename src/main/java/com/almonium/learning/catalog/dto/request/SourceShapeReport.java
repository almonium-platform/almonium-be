package com.almonium.learning.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.Instant;

/** The book processor's verdict on the shape of the file at {@code downloadUrl}, as its shake-down or ingest found it. */
public record SourceShapeReport(
        @NotBlank @Size(max = 1000) String downloadUrl,
        @NotBlank @Pattern(regexp = "ok|suspect|unreadable") String verdict,
        Instant checkedAt) {}
