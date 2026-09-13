package com.almonium.learning.book.dto.response;

/** A file streamed through from the processor, with the name and type it arrived with. */
public record StoredFile(byte[] content, String filename, String contentType) {}
