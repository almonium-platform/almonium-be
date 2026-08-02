package com.almonium.learning.book.dto.request;

import java.util.UUID;

public record BookImportEventRequest(
        UUID importId, UUID ownerId, String status, String title, int progress, int wordCount, String error) {}
