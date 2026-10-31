package com.almonium.analyzer.analyzer.dto;

import java.util.List;

/**
 * A word said exactly like the one on the sheet: {@code varieties} that say them alike (BCP-47 tags), and the
 * {@code senses} (their {@code DiscoverSenseResponse.index}) of the lexemes it sounds like, since lead the metal
 * sounds like led and lead the verb does not.
 */
public record DiscoverHomophoneResponse(String word, List<String> varieties, List<Integer> senses) {}
