package com.almonium.learning.book.model.enums;

import java.util.Set;

/**
 * A reader's ask for a book the shelf does not have (G19). OPEN waits for an operator; IN_PROGRESS means staff took
 * it to the editorial catalogue; PUBLISHED is settled by the edition's publication; DECLINED rows stay only so /ops
 * can count them. Nobody was promised anything, so a decline sends nothing.
 */
public enum BookRequestStatus {
    OPEN,
    IN_PROGRESS,
    PUBLISHED,
    DECLINED;

    /** The states in which an ask still counts: a member cannot ask twice while one of these is standing. */
    public static final Set<BookRequestStatus> STANDING = Set.of(OPEN, IN_PROGRESS);
}
