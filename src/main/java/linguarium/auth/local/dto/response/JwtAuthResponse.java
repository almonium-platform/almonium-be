package linguarium.auth.local.dto.response;

import linguarium.user.core.dto.UserInfo;

public record JwtAuthResponse(String accessToken, UserInfo userInfo) {}
