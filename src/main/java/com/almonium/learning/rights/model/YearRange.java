package com.almonium.learning.rights.model;

/**
 * The first year a work is free somewhere, as tight as the facts allow: {@code earliest} is the soonest it can be free
 * and {@code latest} the latest it can still be protected until. A null bound is open: nothing caps it.
 */
public record YearRange(Integer earliest, Integer latest) {
    public static YearRange exactly(int year) {
        return new YearRange(year, year);
    }

    public static YearRange open() {
        return new YearRange(null, null);
    }

    /** Free when even the latest possible expiry has passed; protected when even the earliest is still ahead. */
    public RightsStatus statusIn(int year) {
        if (latest != null && latest <= year) {
            return RightsStatus.FREE;
        }
        if (earliest != null && earliest > year) {
            return RightsStatus.PROTECTED;
        }
        return RightsStatus.UNKNOWN;
    }
}
