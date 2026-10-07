package com.almonium.learning.wanted.dto.response;

import java.util.List;

/**
 * The Wanted page (W3): up to two works being prepared, the queue in order, what arrived recently, and how many
 * rows the viewer wants. Order and state only, never dates: rank is a strong hint to editorial, not a contract.
 */
public record WantedQueueDto(
        List<WantedRow> beingPrepared, List<WantedRow> queue, List<ArrivedRow> recentlyArrived, int mineCount) {}
