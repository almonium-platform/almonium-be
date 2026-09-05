package com.almonium.subscription.dto.response;

import com.almonium.subscription.model.entity.enums.CadenceChangeOption;
import java.time.Instant;

/**
 * One way to make the change, with the figures Paddle returned for it rather than any we worked out.
 *
 * <p>Amounts stay in minor units beside their currency code: the client formats them, and neither side rounds a
 * currency whose exponent it has not been told.
 *
 * @param creditMinorUnits proration credit for the time already paid for, null when there is none
 * @param refundMinorUnits null unless a refund is part of this option
 * @param effectiveAt when the new cadence starts
 * @param nextBilledAt when the next charge lands, which is not the same date whenever the change is immediate
 */
public record CadenceChangeOptionDto(
        CadenceChangeOption kind,
        boolean recommended,
        long dueNowMinorUnits,
        Long creditMinorUnits,
        Long refundMinorUnits,
        Instant effectiveAt,
        Instant nextBilledAt) {}
