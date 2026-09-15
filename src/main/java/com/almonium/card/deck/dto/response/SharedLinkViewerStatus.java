package com.almonium.card.deck.dto.response;

import java.util.List;
import java.util.UUID;

/**
 * What a signed-in viewer already has of a shared object. {@code heldWordIds} are the shared words whose form the
 * viewer already keeps in this language; {@code dueAmongHeld} is how many of those are due for review now.
 */
public record SharedLinkViewerStatus(boolean owner, boolean hasLearner, List<UUID> heldWordIds, int dueAmongHeld) {}
