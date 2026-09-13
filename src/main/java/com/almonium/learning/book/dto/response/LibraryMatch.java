package com.almonium.learning.book.dto.response;

import java.util.UUID;

/** A library book that already carries the suggested work, so the suggestion needs pointing, not ingesting. */
public record LibraryMatch(UUID bookId, String editionSlug, String title) {}
