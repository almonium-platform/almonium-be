package com.almonium.learning.rights.dto.response;

import com.almonium.learning.catalog.model.CatalogAuthor;
import com.almonium.learning.rights.model.RightsStatus;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Everything /ops needs to judge one shelf work's rights: the facts the engine used and where they came from, the
 * verdict where we make books, the countries it is hidden in and why, and the overrides in force.
 *
 * @param protectedUntil country to the first year the work is free there, for every country that protects it
 * @param unknownCountries countries where the facts are too thin to say
 */
public record WorkRightsView(
        String workSlug,
        String title,
        String author,
        UUID catalogWorkId,
        String wikidataId,
        Integer publicationYear,
        List<CatalogAuthor> authors,
        RightsStatus productionStatus,
        Integer productionFreeFrom,
        Map<String, String> blockedCountries,
        Map<String, Integer> protectedUntil,
        List<String> unknownCountries,
        List<RightsOverrideRow> overrides) {}
