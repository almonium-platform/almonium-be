package com.almonium.learning.lexicon.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/** How far a dictionary row has been checked: written by a model, confirmed by a reviewer, or disputed. */
public enum LexiconStatus {
    PROVISIONAL,
    VERIFIED,
    FLAGGED;

    /** The vocabulary the tables and the API share: lowercase. */
    @JsonValue
    public String label() {
        return name().toLowerCase();
    }

    public static LexiconStatus fromLabel(String label) {
        return valueOf(label.toUpperCase());
    }
}
