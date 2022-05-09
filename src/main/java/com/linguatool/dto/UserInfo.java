package com.linguatool.dto;

import lombok.Value;

import java.util.List;

@Value
public class UserInfo {
	private String id, username, email;
	private List<String> roles;
}
