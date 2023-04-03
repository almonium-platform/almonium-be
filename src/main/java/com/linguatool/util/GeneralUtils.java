package com.linguatool.util;

import com.linguatool.model.dto.SocialProvider;
import com.linguatool.model.entity.user.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class GeneralUtils {

    public static String generateId() {
        return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
    }

    public static String queryBuilder(String httpUrl, List<String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(httpUrl);
        for (String param : params) {
            builder.queryParam(param, "{" + param + "}");
        }
        return builder.encode().toUriString();
    }

    public static String queryBuilder(String httpUrl, Set<String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(httpUrl);
        for (String param : params) {
            builder.queryParam(param, "{" + param + "}");
        }
        return builder.encode().toUriString();
    }

    public static List<SimpleGrantedAuthority> buildSimpleGrantedAuthorities(final Set<Role> roles) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
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
