package com.linguarium.auth.dto.response;

import com.linguarium.auth.dto.UserInfo;
import lombok.Value;

@Value
public class JwtAuthenticationResponse {
	String accessToken;
	UserInfo user;
}
