package com.almonium.learning.rights.exception;

/** The book exists but may not be read from the country the request comes from; answered with 451. */
public class UnavailableInCountryException extends RuntimeException {
    public UnavailableInCountryException() {
        super("This book isn’t available in your country.");
    }
}
