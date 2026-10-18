package com.almonium.analyzer.analyzer.dto;

/** A sentence showing the sense, with its rendering in the translation language when the lexicon has one. */
public record DiscoverExampleResponse(String text, String translation) {}
