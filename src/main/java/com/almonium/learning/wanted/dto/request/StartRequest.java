package com.almonium.learning.wanted.dto.request;

import java.util.UUID;

/** /ops starting a wanted row, optionally naming the free text the book processor should fetch for it. */
public record StartRequest(UUID sourceId) {}
