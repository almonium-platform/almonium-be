package com.almonium.auth.oauth2.apple.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JwksUtil {
    private static final ConcurrentMap<String, RSAPublicKey> keyCache = new ConcurrentHashMap<>();

    public RSAPublicKey getPublicKey(String jwksUrl, String kid) {
        return keyCache.computeIfAbsent(kid, k -> {
            try {
                return fetchPublicKey(jwksUrl, kid);
            } catch (Exception e) {
                throw new IllegalStateException("Failed to retrieve public key from JWKS", e);
            }
        });
    }

    @SneakyThrows
    private RSAPublicKey fetchPublicKey(String jwksUrl, String kid) {
        URL url = new URL(jwksUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (InputStream inputStream = connection.getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jwks = mapper.readTree(inputStream);

            for (JsonNode key : jwks.get("keys")) {
                if (kid.equals(key.get("kid").asText())) {
                    return createPublicKey(key);
                }
            }
        }

        throw new IllegalStateException("Public key with specified kid not found in JWKS");
    }

    @SneakyThrows
    private RSAPublicKey createPublicKey(JsonNode key) {
        String n = key.get("n").asText();
        String e = key.get("e").asText();

        BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(n));
        BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(e));

        RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) factory.generatePublic(spec);
    }
}
