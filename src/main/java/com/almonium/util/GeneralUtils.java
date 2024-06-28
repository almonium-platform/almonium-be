package com.almonium.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;

@UtilityClass
public class GeneralUtils {
    private final String BEARER_TOKEN_PREFIX = "Bearer ";

    public String queryBuilder(String httpUrl, Collection<String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(httpUrl);
        params.forEach(param -> builder.queryParam(param, "{" + param + "}"));
        return builder.encode().toUriString();
    }

    public String bearerOf(String token) {
        return BEARER_TOKEN_PREFIX + token;
    }

    public String tokenFromAuthorizationHeader(String header) {
        return header.substring(GeneralUtils.BEARER_TOKEN_PREFIX.length());
    }
}
