package com.almonium.learning.lexicon.dto;

/** The comparison ledger summed for one model over a window. */
public record SenseComparisonSpendLine(String model, long comparisons, long promptTokens, long completionTokens) {}
