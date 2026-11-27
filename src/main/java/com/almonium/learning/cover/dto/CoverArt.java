package com.almonium.learning.cover.dto;

/** A work that has cover art, and where its subject sits in the image, in percent from the left and the top. */
public record CoverArt(String workSlug, int focusX, int focusY) {}
