package com.linguarium.util;

import com.linguarium.auth.dto.SocialProvider;
import lombok.experimental.UtilityClass;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.UUID;

@UtilityClass
public class GeneralUtils {

    public static String generateId() {
        return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
    }

    public static String queryBuilder(String httpUrl, Collection<String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(httpUrl);
        for (String param : params) {
            builder.queryParam(param, "{" + param + "}");
        }
        return builder.encode().toUriString();
    }

    public static SocialProvider toSocialProvider(String providerId) {
        for (SocialProvider socialProvider : SocialProvider.values()) {
            if (socialProvider.getProviderType().equals(providerId)) {
                return socialProvider;
            }
        }
        return SocialProvider.LOCAL;
    }
}
