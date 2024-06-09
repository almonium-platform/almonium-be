package com.almonium.util;

import java.util.Collection;
import lombok.experimental.UtilityClass;
import org.springframework.web.util.UriComponentsBuilder;

@UtilityClass
public class GeneralUtils {
    public static String queryBuilder(String httpUrl, Collection<String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(httpUrl);
        for (String param : params) {
            builder.queryParam(param, "{" + param + "}");
        }
        return builder.encode().toUriString();
    }
}
