package com.linguarium.auth.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;

@Getter
public class LocalUser extends User implements OAuth2User, OidcUser {
    private final OidcIdToken idToken;
    private final OidcUserInfo userInfo;
    @Setter
    private Map<String, Object> attributes;
    @Getter
    private final com.linguarium.user.model.User user;

    public LocalUser(final String userID, final String password, final com.linguarium.user.model.User user) {
        this(userID, password, user, null, null);
    }

    public LocalUser(final String userID, final String password, final com.linguarium.user.model.User user, OidcIdToken idToken,
                     OidcUserInfo userInfo) {
        super(userID, password, true, true, true, true, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.user = user;
        this.idToken = idToken;
        this.userInfo = userInfo;
    }

    public static LocalUser create(com.linguarium.user.model.User user, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) {
        LocalUser localUser = new LocalUser(user.getEmail(), user.getPassword(), user, idToken, userInfo);
        localUser.setAttributes(attributes);
        return localUser;
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
