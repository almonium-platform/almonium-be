package com.almonium.learning.wanted.dto.response;

import com.almonium.analyzer.translator.model.enums.Language;
import java.util.UUID;

/**
 * A book arriving while its reader waits.
 *
 * @param preparationId what the reader asks about again; null for a book that was on the shelf already
 * @param state where it stands
 * @param step what is being done to it while it is {@link State#ARRIVING}
 * @param editionSlug the book to open once it is {@link State#READY}
 * @param error why it failed, in a sentence a reader can be shown
 * @param sourceKind the library the text is from; after a fallback not the one first asked for
 */
public record LiveBook(
        UUID preparationId,
        UUID catalogWorkId,
        Language language,
        State state,
        Step step,
        String editionSlug,
        String error,
        String sourceKind,
        String sourcePageUrl) {

    public enum State {
        /** Being fetched and made readable now; ask again in a second. */
        ARRIVING,
        /** On the shelf: open {@code editionSlug}. */
        READY,
        /** It did not arrive; {@code error} says why, and asking again starts over. */
        FAILED,
        /** Somebody is preparing the whole book the slow way; it will arrive like any wanted book. */
        BEING_PREPARED
    }

    public enum Step {
        /** The text is being fetched from its library. */
        FETCHING,
        /** The file is being read into chapters and paragraphs. */
        CHAPTERS,
        /** The paragraphs are being split into sentences. */
        SENTENCES,
        /** The book is being put on the shelf. */
        SHELVING
    }
}
