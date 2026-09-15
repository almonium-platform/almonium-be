package com.almonium.learning.rhythm.dto.response;

import java.time.LocalDate;

/**
 * @param minutes learning time on that day, texture for the band's tint and never a threshold
 */
public record RhythmDay(LocalDate date, int minutes, boolean met) {}
