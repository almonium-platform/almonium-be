package com.almonium.auth.local.dto.response;

import com.almonium.user.core.dto.UserInfo;

public record JwtAuthResponse(String accessToken, UserInfo userInfo) {}
