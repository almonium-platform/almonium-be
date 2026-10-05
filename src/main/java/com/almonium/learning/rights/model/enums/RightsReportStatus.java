package com.almonium.learning.rights.model.enums;

/** A report waits OPEN until /ops acts on it (a block was placed) or dismisses it. */
public enum RightsReportStatus {
    OPEN,
    ACTIONED,
    DISMISSED
}
