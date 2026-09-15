package com.almonium.learning.almo.dto;

/**
 * What became of a request for a reply. The reply itself arrives through Stream; {@code ceilingReached} is the one
 * case where nothing will, and the client is expected to say nothing about it.
 */
public record AlmoReplyDto(String replyMessageId, boolean ceilingReached) {}
