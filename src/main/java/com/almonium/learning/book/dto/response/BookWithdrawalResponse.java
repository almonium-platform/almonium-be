package com.almonium.learning.book.dto.response;

import java.util.UUID;

/**
 * Whether this call withdrew a published book. False means there was nothing left to withdraw, which
 * is a success for the caller: the processor may go on and destroy the edition's content.
 */
public record BookWithdrawalResponse(boolean withdrawn, UUID bookId) {}
