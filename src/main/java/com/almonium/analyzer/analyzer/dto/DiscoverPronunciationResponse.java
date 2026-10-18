package com.almonium.analyzer.analyzer.dto;

/**
 * How one variety says the looked-up word, with the best clip the lexicon has for it: {@code clipUrl} is the path
 * under the API base ({@code /public/lexicon/clips/{id}}) or null when nothing was synthesised yet, and
 * {@code clipStatus} says whether that clip was heard back as the word ({@code verified}) or not.
 */
public record DiscoverPronunciationResponse(String variety, String ipa, String clipUrl, String clipStatus) {}
