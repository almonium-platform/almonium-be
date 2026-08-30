package com.almonium.learning.rhythm.dto.request;

import com.almonium.learning.rhythm.model.ActivitySource;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * A report of learning that just happened.
 *
 * @param source where the activity came from
 * @param seconds active seconds since the previous report, already idle-filtered by the client
 * @param localDate the learner's own calendar date, so days are theirs rather than UTC's
 * @param completed whether this report closes a discrete event, such as a finished review session
 */
public record LearningActivityRequest(
        @NotNull ActivitySource source,
        @Min(0) @Max(3600) int seconds,
        @NotNull LocalDate localDate,
        boolean completed) {}
