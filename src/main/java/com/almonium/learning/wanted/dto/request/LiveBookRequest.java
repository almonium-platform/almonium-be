package com.almonium.learning.wanted.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

/**
 * "Read it now": a catalogue work that is not on the shelf, in its original. {@code sourceDownloadUrl} names one free
 * text the reader chose (as a want does); absent, the best free text of the original is taken and the next ones
 * stand by in case its library cannot be had.
 */
public record LiveBookRequest(
        @NotNull UUID catalogWorkId, @Size(max = 1000) String sourceDownloadUrl) {}
