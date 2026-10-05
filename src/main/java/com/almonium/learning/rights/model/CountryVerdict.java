package com.almonium.learning.rights.model;

/**
 * The engine's answer for one work in one country: the status this year, and the year it becomes (or became) free
 * when the facts pin it down.
 */
public record CountryVerdict(String country, RightsStatus status, YearRange freeFrom) {

    /**
     * The year to show beside the verdict: the year it became free, or the soonest it can become free while
     * protected; none while unknown.
     */
    public Integer shownYear() {
        return switch (status) {
            case FREE -> freeFrom.latest();
            case PROTECTED -> freeFrom.earliest();
            case UNKNOWN -> null;
        };
    }
}
