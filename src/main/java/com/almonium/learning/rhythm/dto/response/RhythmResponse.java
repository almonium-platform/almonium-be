package com.almonium.learning.rhythm.dto.response;

import java.util.List;

/**
 * @param target days per week the learner set: {@code null} when never chosen, {@code 0} for "no target"
 * @param weeks oldest first, ending with the week in progress
 */
public record RhythmResponse(Integer target, List<RhythmWeek> weeks) {}
