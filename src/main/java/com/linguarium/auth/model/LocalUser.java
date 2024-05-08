package com.linguarium.auth.model;

import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LocalUser extends User implements OAuth2User, OidcUser {
    private static final String PLACEHOLDER = "OAUTH2_PLACEHOLDER";
    OidcIdToken idToken;
    OidcUserInfo userInfo;
    Map<String, Object> attributes;
    com.linguarium.user.model.User user;

    public LocalUser(com.linguarium.user.model.User user) {
        this(user, Map.of(), null, null);
    }

    public LocalUser(
            com.linguarium.user.model.User user,
            Map<String, Object> attributes,
            OidcIdToken idToken,
            OidcUserInfo userInfo) {
        super(
                user.getEmail(),
                user.getPassword() == null ? PLACEHOLDER : user.getPassword(),
                true,
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.user = user;
        this.idToken = idToken;
        this.userInfo = userInfo;
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return this.user.getUsername();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Map<String, Object> getClaims() {
        return this.attributes;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return this.userInfo;
    }

    @Override
    public OidcIdToken getIdToken() {
        return this.idToken;
    }
}
