package com.almonium.learning.rights.dto.response;

import com.almonium.learning.rights.model.enums.ReporterRole;
import com.almonium.learning.rights.model.enums.RightsReportStatus;
import java.time.Instant;
import java.util.UUID;

/** One report in the /ops rights queue. */
public record RightsReportRow(
        UUID id,
        String workSlug,
        String editionSlug,
        String bookTitle,
        ReporterRole role,
        String reporterName,
        String reporterEmail,
        String country,
        String detectedCountry,
        String message,
        RightsReportStatus status,
        Instant createdAt,
        Instant handledAt) {}
