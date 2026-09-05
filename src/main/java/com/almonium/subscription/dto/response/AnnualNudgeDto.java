package com.almonium.subscription.dto.response;

/**
 * Whether this member has earned the offer to move to annual billing.
 *
 * @param completedSessions finished review sessions, the measure of use the offer is tied to
 * @param requiredSessions the count that unlocks it, sent so the client never hardcodes the threshold
 */
public record AnnualNudgeDto(boolean eligible, long completedSessions, int requiredSessions) {}
