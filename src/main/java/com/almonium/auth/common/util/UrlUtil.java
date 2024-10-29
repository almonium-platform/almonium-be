package com.almonium.auth.common.util;

import org.springframework.web.util.UriComponentsBuilder;

public class UrlUtil {

    public static String addQueryParam(String url, String paramName, String paramValue) {
        return UriComponentsBuilder.fromUriString(url)
                .queryParam(paramName, paramValue)
                .build()
                .toUriString();
    }
}