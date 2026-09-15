package com.almonium.card.deck.service;

import java.security.SecureRandom;
import org.springframework.stereotype.Component;

/** Short, unguessable ids for links people paste into chats: eight base-62 characters, about 47 bits. */
@Component
public class ShareIdGenerator {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LENGTH = 8;
    private final SecureRandom random = new SecureRandom();

    public String next() {
        StringBuilder id = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            id.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return id.toString();
    }
}
