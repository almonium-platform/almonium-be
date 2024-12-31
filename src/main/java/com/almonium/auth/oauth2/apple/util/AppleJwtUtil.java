package com.almonium.auth.oauth2.apple.util;

import static com.almonium.auth.oauth2.other.model.userinfo.OAuth2UserInfo.EMAIL;
import static com.almonium.auth.oauth2.other.model.userinfo.OAuth2UserInfo.EMAIL_VERIFIED;
import static com.almonium.auth.oauth2.other.model.userinfo.OAuth2UserInfo.SUB;
import static lombok.AccessLevel.PRIVATE;

import com.almonium.config.properties.AppProperties;
import com.almonium.config.properties.AppleOAuthProperties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AppleJwtUtil {
    AppProperties appProperties;
    AppleOAuthProperties appleOAuthProperties;

    @SneakyThrows
    public Map<String, Object> verifyAndParseToken(String idToken) {
        RSAPublicKey publicKey = JwksUtil.getPublicKey(
                appleOAuthProperties.getProvider().getJwkSetUri(),
                JWT.decode(idToken).getKeyId());

        Algorithm algorithm = Algorithm.RSA256(publicKey, null);

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(appProperties.getAuth().getOauth2().getAppleTokenUrl())
                .withAudience(appProperties.getAuth().getOauth2().getAppleServiceId())
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
