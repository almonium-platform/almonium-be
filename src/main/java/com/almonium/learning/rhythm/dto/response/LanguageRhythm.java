package com.almonium.learning.rhythm.dto.response;

import com.almonium.analyzer.translator.model.enums.Language;
import java.util.List;

/**
 * One language's harness.
 *
 * @param target days per week asked of this language: {@code null} when never chosen, {@code 0} for "no target"
 * @param editable whether the bar can still be moved; a set-aside language keeps its record, read-only
 * @param weeks oldest first, ending with the week in progress
 */
public record LanguageRhythm(Language language, Integer target, boolean editable, List<RhythmWeek> weeks) {}
