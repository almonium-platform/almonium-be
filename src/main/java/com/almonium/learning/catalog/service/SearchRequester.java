package com.almonium.learning.catalog.service;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Tells searchers apart in the search log without keeping anything that names them: a member by id, a guest by a
 * keyed hash of their address under a key that lives only as long as the process. Enough to count distinct readers
 * and to fold someone's half-typed queries into the one they finished; useless for finding the person.
 */
@Component
public class SearchRequester {
    private static final SecureRandom RANDOM = new SecureRandom();

    private final byte[] key = new byte[32];

    public SearchRequester() {
        RANDOM.nextBytes(key);
    }

    public String of(UUID memberId) {
        if (memberId != null) {
            return "m:" + memberId;
        }
        String address = RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes
                ? attributes.getRequest().getRemoteAddr()
                : "";
        return "g:" + hash(address);
    }

    String hash(String address) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key, "HmacSHA256"));
            return HexFormat.of().formatHex(mac.doFinal(address.getBytes(StandardCharsets.UTF_8)), 0, 16);
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("HmacSHA256 unavailable", exception);
        }
    }
}
