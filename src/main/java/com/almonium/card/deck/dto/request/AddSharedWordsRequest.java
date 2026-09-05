package com.almonium.card.deck.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

public record AddSharedWordsRequest(@NotEmpty List<UUID> wordIds) {}
