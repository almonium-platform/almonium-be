package com.almonium.learning.lexicon.model;

/**
 * Where a lexeme, form or sense is used when not everywhere its language is: a variety of the product's catalogue
 * (en-GB, pt-BR) and whether the use is {@code only} there or {@code mainly} there.
 */
public record RegionScope(String variety, String extent) {}
