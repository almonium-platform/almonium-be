package com.almonium.learning.lexicon.audio;

/** One clip as the store hands it back: the bytes and what they are. */
public record StoredAudio(byte[] bytes, String contentType) {}
