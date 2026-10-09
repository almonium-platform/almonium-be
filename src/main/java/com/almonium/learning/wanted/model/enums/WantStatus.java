package com.almonium.learning.wanted.model.enums;

import java.util.List;

/**
 * Where a want stands. OPEN waits in the public queue; IN_PROGRESS is being prepared (editorial started it);
 * ARRIVED was settled by a published edition; DECLINED was passed on by editorial and leaves the queue.
 */
public enum WantStatus {
    OPEN,
    IN_PROGRESS,
    ARRIVED,
    DECLINED;

    /** The statuses that still count toward the queue. */
    public static final List<WantStatus> STANDING = List.of(OPEN, IN_PROGRESS);
}
