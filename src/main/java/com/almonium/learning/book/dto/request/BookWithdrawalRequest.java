package com.almonium.learning.book.dto.request;

import jakarta.validation.constraints.NotBlank;

public record BookWithdrawalRequest(@NotBlank String editionSlug) {}
