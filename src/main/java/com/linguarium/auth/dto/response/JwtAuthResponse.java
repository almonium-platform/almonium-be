package com.linguarium.auth.dto.response;

import com.linguarium.auth.dto.UserInfo;

public record JwtAuthResponse(String accessToken, UserInfo userInfo) {}
