package com.linguatool.model.dto;

import lombok.Value;

import java.util.List;

@Value
public class UserInfo {
	String id, username, email, ui_lang;
	List<String> roles, tags, targetLangs, fluentLangs;
}
