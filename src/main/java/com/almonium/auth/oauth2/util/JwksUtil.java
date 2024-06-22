package com.almonium.auth.oauth2.util;

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

public class JwksUtil {
    private static final ConcurrentMap<String, RSAPublicKey> keyCache = new ConcurrentHashMap<>();

    public static RSAPublicKey getPublicKey(String jwksUrl, String kid) throws Exception {
        if (keyCache.containsKey(kid)) {
            return keyCache.get(kid);
        }

        URL url = new URL(jwksUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        InputStream inputStream = connection.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jwks = mapper.readTree(inputStream);

        for (JsonNode key : jwks.get("keys")) {
            if (kid.equals(key.get("kid").asText())) {
                String n = key.get("n").asText();
                String e = key.get("e").asText();

                BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(n));
                BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(e));

                RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
                KeyFactory factory = KeyFactory.getInstance("RSA");
                RSAPublicKey publicKey = (RSAPublicKey) factory.generatePublic(spec);

                keyCache.put(kid, publicKey);
                return publicKey;
            }
        }

        throw new IllegalStateException("Failed to retrieve public key from JWKS");
    }
}
