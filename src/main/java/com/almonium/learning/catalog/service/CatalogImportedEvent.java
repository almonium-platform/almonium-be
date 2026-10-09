package com.almonium.learning.catalog.service;

/** A catalogue import finished: facts behind the shelf's copyright verdicts may have changed. */
public record CatalogImportedEvent(int works) {}
