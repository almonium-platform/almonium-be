package com.almonium.learning.rights.dto.response;

import java.util.UUID;

/** What the reporter is told: the report's id and where to write with more. */
public record RightsReportReceipt(UUID id, String contact) {}
