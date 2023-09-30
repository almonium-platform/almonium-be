package com.linguarium.auth.dto;

import lombok.Value;

import java.util.List;

@Value
public class UserInfo {
	String id, username, email, uiLang, profilePicLink, background;
	Integer streak;
	List<String> roles, tags, targetLangs, fluentLangs;
}
