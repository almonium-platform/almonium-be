package com.almonium.card.deck.dto.response;

/** Who shared it: handle, avatar and whether the member star is drawn. Nothing else about the person travels. */
public record SharerDto(String username, String avatarUrl, boolean premium) {}
