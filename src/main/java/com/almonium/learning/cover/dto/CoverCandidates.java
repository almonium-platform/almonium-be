package com.almonium.learning.cover.dto;

import java.util.List;

/** One work and the files a judge may choose its cover from; {@code note} says why there are none. */
public record CoverCandidates(
        String workSlug, String title, String author, List<CoverCandidate> candidates, String note) {}
