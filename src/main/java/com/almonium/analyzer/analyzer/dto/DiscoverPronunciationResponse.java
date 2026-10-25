package com.almonium.analyzer.analyzer.dto;

import java.util.List;

/**
 * How one variety says the looked-up word, with the best clip the lexicon has for it: {@code clipUrl} is the path
 * under the API base ({@code /public/lexicon/clips/{id}}) or null when nothing was synthesised yet, and
 * {@code clipStatus} says whether that clip was heard back as the word ({@code verified}) or not. {@code segments}
 * cut {@code ipa} where it parts from the other varieties' transcriptions of the word (null without one).
 * {@code senses} are the {@link DiscoverSenseResponse#index() numbers} of the senses said this way: two readings of a
 * variety with the same senses are free alternatives (niche), different senses mean the reading follows the meaning
 * (live, bow).
 */
public record DiscoverPronunciationResponse(
        String variety,
        String ipa,
        String clipUrl,
        String clipStatus,
        List<IpaSegmentResponse> segments,
        List<Integer> senses) {

    public DiscoverPronunciationResponse withSegments(List<IpaSegmentResponse> segments) {
        return new DiscoverPronunciationResponse(variety, ipa, clipUrl, clipStatus, segments, senses);
    }

    public DiscoverPronunciationResponse withSenses(List<Integer> senses) {
        return new DiscoverPronunciationResponse(variety, ipa, clipUrl, clipStatus, segments, senses);
    }
}
