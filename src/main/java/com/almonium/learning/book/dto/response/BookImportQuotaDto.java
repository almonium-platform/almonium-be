package com.almonium.learning.book.dto.response;

/** The caller's private-book allowance for the current subscription month. */
/** The private cap: how many imports may stand on the shelf at a time, and how many do. */
public record BookImportQuotaDto(int limit, int used) {}
