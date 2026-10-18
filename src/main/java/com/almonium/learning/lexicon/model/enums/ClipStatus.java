package com.almonium.learning.lexicon.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Whether the synthesiser's clip was heard back as the word it was meant to say: the audio phase transcribes each
 * clip and compares. A clip that failed the comparison is kept as unverified, with what was heard, so it can be
 * judged later rather than paid for again.
 */
public enum ClipStatus {
    VERIFIED,
    UNVERIFIED;

    /** The vocabulary the table and the API share: lowercase. */
    @JsonValue
    public String label() {
        return name().toLowerCase();
    }

    public static ClipStatus fromLabel(String label) {
        try {
            return valueOf(label.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw new IllegalArgumentException("Unknown clip status: " + label + " (verified or unverified)");
        }
    }
}
