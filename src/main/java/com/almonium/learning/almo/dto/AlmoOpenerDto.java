package com.almonium.learning.almo.dto;

/** A chip above the composer. {@code word} is the queue word inside it, if any, so the client can mark provenance. */
public record AlmoOpenerDto(String text, String word) {}
