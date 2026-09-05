package com.almonium.card.deck.dto.response;

import java.util.List;
import java.util.UUID;

/**
 * A word as a stranger may see it: the entry, its senses and examples, and nothing about how the owner is doing with
 * it. Review state, intents and tags never leave the owner's account.
 */
public record SharedWordDto(
        UUID id,
        String entry,
        String partOfSpeech,
        String selectedSense,
        List<String> translations,
        List<SharedExampleDto> examples,
        String sourceContext) {}
