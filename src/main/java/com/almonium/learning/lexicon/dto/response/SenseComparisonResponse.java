package com.almonium.learning.lexicon.dto.response;

/**
 * How two senses differ, in a few sentences. {@code summary} is null when none was bought yet and none was asked
 * for, or when {@code capped}: the member has had today's share of new explanations.
 */
public record SenseComparisonResponse(String summary, boolean capped) {}
