package com.almonium.auth.common.service;

import com.almonium.auth.token.dto.response.JwtTokenResponse;
import com.almonium.user.core.model.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface UserAuthenticationService {
    JwtTokenResponse authenticateUser(User user, HttpServletResponse response, Authentication authentication);
}
