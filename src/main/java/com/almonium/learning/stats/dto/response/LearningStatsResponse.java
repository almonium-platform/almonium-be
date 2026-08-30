package com.almonium.learning.stats.dto.response;

/**
 * The numbers on a learner's profile that only move by reading.
 *
 * @param wordsKept vocabulary kept in this language
 * @param booksFinished books read to the end in this language
 */
public record LearningStatsResponse(long wordsKept, long booksFinished) {}
