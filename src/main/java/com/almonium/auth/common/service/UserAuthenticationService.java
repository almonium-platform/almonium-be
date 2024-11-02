package com.almonium.auth.common.service;

import com.almonium.auth.common.model.entity.Principal;
import com.almonium.auth.token.dto.response.JwtTokenResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface UserAuthenticationService {
    JwtTokenResponse authenticateUser(Principal principal, HttpServletResponse response, Authentication authentication);
}
