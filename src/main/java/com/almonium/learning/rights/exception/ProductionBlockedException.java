package com.almonium.learning.rights.exception;

/** A work under copyright where editions are made or hosted was about to be published. */
public class ProductionBlockedException extends RuntimeException {
    public ProductionBlockedException(String message) {
        super(message);
    }
}
