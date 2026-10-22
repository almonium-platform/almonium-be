package com.almonium.learning.catalog.dto.response;

import com.almonium.learning.rights.service.UnlockDates.Unlock;
import java.util.List;
import java.util.UUID;

/**
 * Where and when one catalogue work is free to read (W7): every country's status, the year it frees up when the facts
 * pin it down, and the rule behind it. {@code readerCountry} is where this request comes from; {@code readerFreeFrom}
 * the year the reader can open it (free where it is made and in their country), when exact.
 */
public record WorkUnlocksView(
        UUID id,
        String title,
        String author,
        Integer year,
        String readerCountry,
        Integer readerFreeFrom,
        List<Unlock> countries) {}
