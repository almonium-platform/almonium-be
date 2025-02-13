package com.almonium.auth.local.dto.response;

import com.almonium.user.core.dto.response.UserInfo;

public record JwtAuthResponse(String accessToken, String refreshToken, UserInfo userInfo) {}
