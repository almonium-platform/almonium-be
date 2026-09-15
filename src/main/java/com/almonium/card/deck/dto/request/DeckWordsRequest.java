package com.almonium.card.deck.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/** The deck's words, in the order the owner wants them shown. Replaces the previous list. */
public record DeckWordsRequest(@NotNull List<UUID> wordIds) {}
