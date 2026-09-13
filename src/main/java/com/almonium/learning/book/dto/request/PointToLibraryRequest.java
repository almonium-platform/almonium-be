package com.almonium.learning.book.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PointToLibraryRequest(@NotNull UUID bookId) {}
