package com.almonium.learning.cover.model;

/** What became of the search for a work's cover art. */
public enum CoverStatus {
    /** An image was chosen and is served. */
    APPROVED,
    /** Nothing passed the licence rule and the model's eye; the book keeps its typographic cover. */
    NONE,
    /** An operator took the chosen image down; it is not looked for again. */
    REJECTED
}
