package com.linguarium.auth.dto.response;

import com.linguarium.auth.dto.UserInfo;

public record JwtAuthenticationResponse(String accessToken, UserInfo userInfo) {}
