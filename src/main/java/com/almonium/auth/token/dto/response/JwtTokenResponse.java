package com.almonium.auth.token.dto.response;

public record JwtTokenResponse(String accessToken, String refreshToken) {
}
