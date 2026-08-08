package com.almonium.auth.common.security;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public final class SecurityRoles {
    private static final SimpleGrantedAuthority DEFAULT_ROLE = new SimpleGrantedAuthority("ROLE_USER");
    private static final SimpleGrantedAuthority ADMIN_ROLE = new SimpleGrantedAuthority("ROLE_ADMIN");
    public static final List<GrantedAuthority> USER = List.of(DEFAULT_ROLE);
    public static final List<GrantedAuthority> ADMIN = List.of(DEFAULT_ROLE, ADMIN_ROLE);

    private SecurityRoles() {}

    public static boolean isAdmin(Authentication authentication) {
        return authentication != null && authentication.getAuthorities().contains(ADMIN_ROLE);
    }
}
