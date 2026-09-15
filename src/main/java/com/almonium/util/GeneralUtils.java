package com.almonium.util;

import java.util.Collection;
import org.springframework.web.util.UriComponentsBuilder;

public final class GeneralUtils {
    private GeneralUtils() {}

    public static String queryBuilder(String httpUrl, Collection<String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(httpUrl);
        params.forEach(param -> builder.queryParam(param, "{" + param + "}"));
        return builder.encode().toUriString();
    }
}
