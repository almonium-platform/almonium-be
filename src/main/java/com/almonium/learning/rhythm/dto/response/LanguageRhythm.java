package com.almonium.learning.rhythm.dto.response;

import com.almonium.analyzer.translator.model.enums.Language;
import java.time.LocalDate;
import java.util.List;

/**
 * One language's harness.
 *
 * @param target days per week asked of this language: {@code null} when never chosen, {@code 0} for "no target"
 * @param editable whether the bar can still be moved; a set-aside language keeps its record, read-only
 * @param startedAt when the language was taken up, so weeks at pace count only the weeks it has existed
 * @param setAsideAt when the language was set aside, or null while it is active
 * @param weeks oldest first, ending with the week in progress
 */
public record LanguageRhythm(
        Language language,
        Integer target,
        boolean editable,
        LocalDate startedAt,
        LocalDate setAsideAt,
        List<RhythmWeek> weeks) {}
