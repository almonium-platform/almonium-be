package com.almonium.learning.book.model.enums;

import java.util.Set;

/**
 * A private import offered for the public library. SUGGESTED waits for an operator; INGESTING is a processor job;
 * PUBLISHED is settled, either by that job's publication or by pointing at a copy the library already had.
 * Withdrawing deletes the row; DECLINED rows stay only so /ops can count them.
 */
public enum LibrarySuggestionStatus {
    SUGGESTED,
    INGESTING,
    PUBLISHED,
    DECLINED;

    /** The one suggestion an import may hold at a time. */
    public static final Set<LibrarySuggestionStatus> OPEN = Set.of(SUGGESTED, INGESTING, PUBLISHED);
}
