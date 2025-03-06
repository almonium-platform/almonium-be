package com.almonium.util;

import java.util.Collection;
import lombok.experimental.UtilityClass;
import org.springframework.web.util.UriComponentsBuilder;

@UtilityClass
public class GeneralUtils {
    public String queryBuilder(String httpUrl, Collection<String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(httpUrl);
        params.forEach(param -> builder.queryParam(param, "{" + param + "}"));
        return builder.encode().toUriString();
    }
}
