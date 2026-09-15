package com.almonium.user.core.dto.response;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * What the account may do with its languages: how many may be active, when the next switch is available, and what
 * there is to choose between.
 *
 * @param allowance how many languages may be active now, or {@code -1} for unlimited
 * @param allowanceWithoutPlan how many survive when the plan ends, so the sheet states the stake rather than assuming it
 * @param nextSwitchAllowedAt when the once-a-month switch comes back, or null when it is available now
 * @param languages every language on the account, most recently read first
 */
public record ActiveLanguagePolicy(
        int allowance, int allowanceWithoutPlan, Instant nextSwitchAllowedAt, List<Choice> languages) {

    /**
     * One language as the downgrade sheet has to show it: the word count and the last-read date are what make the
     * choice answerable.
     */
    public record Choice(
            Language language,
            CEFR cefrLevel,
            long wordsKept,
            LocalDate lastReadOn,
            boolean active,
            boolean recommended) {}
}
