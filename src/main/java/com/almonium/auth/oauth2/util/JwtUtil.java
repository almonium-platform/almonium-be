package com.almonium.auth.oauth2.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${app.oauth2.appleTokenUrl}")
    String appleTokenUrl;

    @Value("${spring.security.oauth2.client.provider.apple.jwk-set-uri}")
    String appleJwkUri;

    @Value("${app.oauth2.appleServiceId}")
    String appleServiceId;

    public Map<String, Object> parseAndVerifyToken(String idToken) throws Exception {
        DecodedJWT decodedJWT = JWT.decode(idToken);
        String kid = decodedJWT.getKeyId();

        RSAPublicKey publicKey = JwksUtil.getPublicKey(appleJwkUri, kid);

        Algorithm algorithm = Algorithm.RSA256(publicKey, null);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(appleTokenUrl)
                .withAudience(appleServiceId)
                .build();
        DecodedJWT jwt = verifier.verify(idToken);

        String email = jwt.getClaim("email").asString();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("email", email);
        return userInfo;
    }
}
