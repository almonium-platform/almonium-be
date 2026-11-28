package com.almonium.learning.play.dto.response;

/** The best after a run was recorded, and the best before it; {@code previousBest} is null on a first run. */
public record HigherLowerRunResult(int best, Integer previousBest) {}
