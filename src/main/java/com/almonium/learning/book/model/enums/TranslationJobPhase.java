package com.almonium.learning.book.model.enums;

import java.util.Set;

/**
 * Where an approved translation stands. The four working phases mirror the processor's own; the rest are ours.
 * /ops decides and watches, it never runs a pipeline, so nothing here is ever advanced by hand.
 */
public enum TranslationJobPhase {
    QUEUED,
    TRANSLATING,
    ALIGNING,
    QA_GATE,
    PUBLISHING,
    PUBLISHED,
    FAILED,
    CANCELLED;

    /** A job the processor is still working on, and therefore the one that blocks a second approval of the pair. */
    public static final Set<TranslationJobPhase> LIVE = Set.of(QUEUED, TRANSLATING, ALIGNING, QA_GATE, PUBLISHING);

    public boolean isLive() {
        return LIVE.contains(this);
    }

    /** The processor's phase word, or QUEUED when it has not said yet. */
    public static TranslationJobPhase fromProcessor(String phase) {
        if (phase == null) {
            return QUEUED;
        }
        return switch (phase) {
            case "translating" -> TRANSLATING;
            case "aligning" -> ALIGNING;
            case "qa_gate" -> QA_GATE;
            case "publishing" -> PUBLISHING;
            case "published" -> PUBLISHED;
            case "failed" -> FAILED;
            default -> QUEUED;
        };
    }
}
