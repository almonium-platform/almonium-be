package com.almonium.learning.lexicon.model.enums;

/** What a member says is wrong with an entry or one of its senses. */
public enum EntryReportReason {
    /** The sense shown is not the one the sentence uses. */
    WRONG_SENSE,
    WRONG_DEFINITION,
    /** Gender, a form, a part of speech or other grammar. */
    WRONG_GRAMMAR,
    WRONG_SOUND,
    BAD_EXAMPLE
}
