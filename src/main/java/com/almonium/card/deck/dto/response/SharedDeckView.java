package com.almonium.card.deck.dto.response;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.deck.model.enums.SharedLinkStatus;
import java.util.List;

/**
 * A deck as its link shows it. A dead link carries only its status: the owner of a revoked or deleted deck is never
 * named to a stranger holding a link with nothing behind it.
 */
public record SharedDeckView(
        SharedLinkStatus status,
        String shareId,
        String title,
        Language language,
        List<SharedWordDto> words,
        SharerDto sharer) {

    public static SharedDeckView dead(SharedLinkStatus status) {
        return new SharedDeckView(status, null, null, null, List.of(), null);
    }
}
