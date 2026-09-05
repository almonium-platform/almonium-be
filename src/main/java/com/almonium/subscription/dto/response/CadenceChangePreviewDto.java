package com.almonium.subscription.dto.response;

import com.almonium.subscription.model.entity.Plan;
import java.time.Instant;
import java.util.List;

/**
 * Everything a cadence confirmation screen is allowed to print. Nothing is confirmed until the cadence, the amount due
 * and the effective date are all on it, so they travel together rather than being assembled client-side.
 *
 * @param targetPriceMinorUnits what the subscription will cost each period after the change, in the member's own
 *     currency as Paddle resolved it
 * @param founderPrice whether that figure is a founding-member price, which the change is required to preserve
 */
public record CadenceChangePreviewDto(
        Plan.Type currentType,
        Plan.Type targetType,
        long targetPriceMinorUnits,
        boolean founderPrice,
        String currencyCode,
        Instant currentPeriodEndsAt,
        List<CadenceChangeOptionDto> options) {}
