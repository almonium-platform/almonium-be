package com.almonium.auth.oauth2.util;

import static com.almonium.auth.oauth2.model.userinfo.OAuth2UserInfo.EMAIL;
import static com.almonium.auth.oauth2.model.userinfo.OAuth2UserInfo.EMAIL_VERIFIED;
import static com.almonium.auth.oauth2.model.userinfo.OAuth2UserInfo.SUB;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppleJwtUtil {
    @Value("${app.oauth2.appleTokenUrl}")
    String appleTokenUrl;

    @Value("${spring.security.oauth2.client.provider.apple.jwk-set-uri}")
    String appleJwkUri;

    @Value("${app.oauth2.appleServiceId}")
    String appleServiceId;

    @SneakyThrows
    public Map<String, Object> verifyAndParseToken(String idToken) {
        RSAPublicKey publicKey =
                JwksUtil.getPublicKey(appleJwkUri, JWT.decode(idToken).getKeyId());

        Algorithm algorithm = Algorithm.RSA256(publicKey, null);

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(appleTokenUrl)
                .withAudience(appleServiceId)
                .build();

        DecodedJWT jwt = verifier.verify(idToken);

        return Map.of(
                EMAIL,
                jwt.getClaim(EMAIL).asString(),
                EMAIL_VERIFIED,
                jwt.getClaim(EMAIL_VERIFIED).asBoolean(),
                SUB,
                jwt.getClaim(SUB).asString());
    }
}
