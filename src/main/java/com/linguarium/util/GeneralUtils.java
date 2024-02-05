package com.linguarium.util;

import java.util.Collection;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import org.springframework.web.util.UriComponentsBuilder;

@UtilityClass
public class GeneralUtils {
    public static String generateId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String queryBuilder(String httpUrl, Collection<String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(httpUrl);
        for (String param : params) {
            builder.queryParam(param, "{" + param + "}");
        }
        return builder.encode().toUriString();
    }
}
