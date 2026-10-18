package com.almonium.learning.lexicon.dto.response;

/** A clip ready to stream: its bytes and the hash of its key, which is what a reader's cache validates against. */
public record ClipContent(byte[] bytes, String etag) {}
