package com.almonium.card.deck.model.enums;

/**
 * What a visitor finds behind a share id. Revoked and deleted are kept apart because the visitor's question differs:
 * a revoked link can be reissued by its owner, a deleted deck cannot come back and its words went with it.
 */
public enum SharedLinkStatus {
    ACTIVE,
    REVOKED,
    DELETED
}
