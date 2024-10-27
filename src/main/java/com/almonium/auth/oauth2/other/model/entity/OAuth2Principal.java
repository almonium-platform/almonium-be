package com.almonium.auth.oauth2.other.model.entity;

import com.almonium.auth.common.model.entity.Principal;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.Collection;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("OAUTH2")
public class OAuth2Principal extends Principal implements OAuth2User {
    String providerUserId;
    String firstName;
    String lastName;

    @Override
    public String getName() {
        return getEmail();
    }

    @Override
    public <A> A getAttribute(String name) {
        throw new NotImplementedException("Attributes are not stored!");
    }

    @Override
    public Map<String, Object> getAttributes() {
        throw new NotImplementedException("Attributes are not stored!");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Principal.ROLES;
    }
}
