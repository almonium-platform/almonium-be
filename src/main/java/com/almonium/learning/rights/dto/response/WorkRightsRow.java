package com.almonium.learning.rights.dto.response;

import com.almonium.learning.rights.model.RightsStatus;

/** One shelf work in the /ops rights list. */
public record WorkRightsRow(
        String workSlug,
        String title,
        String author,
        boolean linked,
        RightsStatus productionStatus,
        boolean blockedEverywhere,
        int blockedCountries,
        int overrides) {}
