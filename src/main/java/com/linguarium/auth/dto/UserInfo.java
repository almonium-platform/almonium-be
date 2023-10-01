package com.linguarium.auth.dto;

import java.util.Collection;

public record UserInfo(String id, String username, String email, String uiLang, String profilePicLink,
					   String background, Integer streak, Collection<String> targetLangs,
					   Collection<String> fluentLangs, Collection<String> tags) {
}
