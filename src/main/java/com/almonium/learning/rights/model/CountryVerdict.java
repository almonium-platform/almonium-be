package com.almonium.learning.rights.model;

/**
 * The engine's answer for one work in one country: the status this year, and the year it becomes (or became) free
 * when the facts pin it down.
 */
public record CountryVerdict(String country, RightsStatus status, YearRange freeFrom) {}
