package com.almonium.card.deck.dto.response;

import java.time.Instant;

/** How an add went: what was copied, what the viewer already had, and when the first copy comes up for review. */
public record AddedWordsResult(int added, int alreadyHeld, Instant firstDueAt) {}
