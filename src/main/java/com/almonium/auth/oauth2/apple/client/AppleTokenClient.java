package com.almonium.auth.oauth2.apple.client;

import com.almonium.auth.oauth2.apple.dto.AppleTokenResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PostExchange;

public interface AppleTokenClient {

    @PostExchange(value = "/auth/token", contentType = "application/x-www-form-urlencoded", accept = "application/json")
    AppleTokenResponse getToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("code") String code,
            @RequestParam("redirect_uri") String redirectUri,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret);
}
