package com.almonium.analyzer.analyzer.dto;

import java.util.List;

/**
 * How one variety says the looked-up word, with the best clip the lexicon has for it: {@code clipUrl} is the path
 * under the API base ({@code /public/lexicon/clips/{id}}) or null when nothing was synthesised yet, and
 * {@code clipStatus} says whether that clip was heard back as the word ({@code verified}) or not. {@code segments}
 * cut {@code ipa} where it parts from the other varieties' transcriptions of the word (null without one).
 */
public record DiscoverPronunciationResponse(
        String variety, String ipa, String clipUrl, String clipStatus, List<IpaSegmentResponse> segments) {

    public DiscoverPronunciationResponse withSegments(List<IpaSegmentResponse> segments) {
        return new DiscoverPronunciationResponse(variety, ipa, clipUrl, clipStatus, segments);
    }
}
