package com.almonium.learning.cover.dto;

/** A Commons file that passed the licence rule: what a judge is shown and what a cover's record keeps. */
public record CoverCandidate(
        String title,
        String pageUrl,
        String imageUrl,
        String mime,
        String artist,
        String licence,
        String licenceBasis,
        /** The Commons category the file was found in, which often says what the picture is. */
        String foundIn) {}
