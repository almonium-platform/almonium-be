package com.almonium.auth.local.model.entity;

import static com.almonium.auth.common.enums.AuthProviderType.LOCAL;

import com.almonium.auth.common.model.entity.Principal;
import com.almonium.user.core.model.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@SuperBuilder
@DiscriminatorValue("LOCAL")
public class LocalPrincipal extends Principal implements UserDetails {
    String password;
    boolean verified;

    public LocalPrincipal() {
        this.setProvider(LOCAL);
    }

    public LocalPrincipal(User user, String email, String password) {
        super(user, email, LOCAL);
        this.password = password;
        this.verified = false;
    }

    // UserDetails methods

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }
}
