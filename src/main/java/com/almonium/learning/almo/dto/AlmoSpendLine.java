package com.almonium.learning.almo.dto;

/** The turn ledger summed for one model over a window. */
public record AlmoSpendLine(String model, long turns, long promptTokens, long completionTokens) {}
