package com.linguatool.util;

import com.linguatool.model.dto.LocalUser;
import com.linguatool.model.dto.SocialProvider;
import com.linguatool.model.dto.UserInfo;
import com.linguatool.model.entity.lang.LanguageEntity;
import com.linguatool.model.entity.user.Language;
import com.linguatool.model.entity.user.Role;
import com.linguatool.model.entity.user.Tag;
import com.linguatool.model.entity.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class GeneralUtils {
    public static String queryBuilder(String httpUrl, List<String> params) {
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

    public static UserInfo buildUserInfo(LocalUser localUser) {
        List<String> roles = localUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        User user = localUser.getUser();
        List<String> tags = user.getTags().stream().map(Tag::getText).collect(Collectors.toList());
        List<String> langs = user.getLearningLanguages().stream().map(t -> t.getCode().getCode()).collect(Collectors.toList());
        return new UserInfo(user.getId().toString(), user.getUsername(), user.getEmail(), user.getUiLanguage().getCode(), roles, tags, langs);
    }
}
