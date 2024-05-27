package com.linguarium.auth.local.dto.response;

import com.linguarium.user.core.dto.UserInfo;

public record JwtAuthResponse(String accessToken, UserInfo userInfo) {}
