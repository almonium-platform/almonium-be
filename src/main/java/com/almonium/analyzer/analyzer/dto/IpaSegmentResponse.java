package com.almonium.analyzer.analyzer.dto;

/** A run of a transcription, and whether another variety's transcription says it differently there. */
public record IpaSegmentResponse(String text, boolean differs) {}
