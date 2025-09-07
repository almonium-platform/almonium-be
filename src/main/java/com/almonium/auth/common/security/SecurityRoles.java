package com.almonium.auth.common.security;

import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public final class SecurityRoles {
    private static final SimpleGrantedAuthority DEFAULT_ROLE = new SimpleGrantedAuthority("ROLE_USER");
    public static final List<GrantedAuthority> USER = List.of(DEFAULT_ROLE);

    private SecurityRoles() {}
}
