package com.almonium.learning.rhythm.dto.response;

/**
 * Weeks kept against weeks asked for, fixed at a moment rather than rolling.
 *
 * <p>A rolling window over a language that has stopped moving decays to nothing and reads as broken, so a
 * set-aside language keeps the fraction it had on the day it was put down.
 */
public record PaceSnapshot(int met, int counted) {}
