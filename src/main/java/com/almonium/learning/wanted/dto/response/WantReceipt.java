package com.almonium.learning.wanted.dto.response;

import com.almonium.analyzer.translator.model.enums.Language;
import java.util.UUID;

/**
 * What the toast says after a want (W4): the work's place in the queue after counting this want, and how many
 * readers want it ({@code readers} is null below three, so a lone want never reads as "1 reader").
 */
public record WantReceipt(UUID catalogWorkId, Language language, int rank, Integer readers) {}
