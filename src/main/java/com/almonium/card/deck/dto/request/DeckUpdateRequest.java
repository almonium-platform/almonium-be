package com.almonium.card.deck.dto.request;

import jakarta.validation.constraints.Size;

/** Both fields optional: a rename leaves the link alone, and flipping the link leaves the title alone. */
public record DeckUpdateRequest(@Size(max = 60) String title, Boolean shareEnabled) {}
