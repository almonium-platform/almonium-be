package com.almonium.learning.rhythm.dto.response;

import java.time.LocalDate;
import java.util.List;

/**
 * @param weekStart the Monday the week begins on
 * @param met whether the learner cleared their own bar that week; always false while no target is set
 * @param frozen a week after the language was set aside: neither met nor missed, because nothing was asked of it
 */
public record RhythmWeek(LocalDate weekStart, int daysMet, boolean met, boolean frozen, List<RhythmDay> days) {}
