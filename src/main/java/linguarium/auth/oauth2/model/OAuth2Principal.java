package linguarium.auth.oauth2.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import linguarium.auth.common.enums.AuthProviderType;
import linguarium.auth.common.model.entity.Principal;
import linguarium.user.core.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

    public OAuth2Principal(
            User user, String email, AuthProviderType provider, String providerUserId, Map<String, Object> attributes) {
        super(user, email, provider);
        this.providerUserId = providerUserId;
        this.attributes = attributes;
    }

    @Transient
    @Builder.Default
    Map<String, Object> attributes = new HashMap<>();

    @Override
    public <A> A getAttribute(String name) {
        try {
            @SuppressWarnings("unchecked")
            A value = (A) attributes.get(name);
            return value;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Attribute " + name + " is not of the expected type", e);
        }
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
