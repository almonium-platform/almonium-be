package com.almonium.learning.book.dto.response;

import java.util.List;
import java.util.UUID;

/** Current reader-safe processor projection; CEFR is an estimate, not an editorial override. */
public record BookChapter(
        UUID id, int sequence, String title, String analysisStatus, String cefrEstimate, List<String> descriptions) {}
