package com.linguatool.controller;

import com.linguatool.config.CurrentUser;
import com.linguatool.dto.LocalUser;
import com.linguatool.model.Friend;
import com.linguatool.model.user.User;
import com.linguatool.service.UserServiceImpl;
import com.linguatool.util.GeneralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

	final UserServiceImpl userService;

	public UserController(UserServiceImpl userService) {
		this.userService = userService;
	}

	@GetMapping("/user/me")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getCurrentUser(@CurrentUser LocalUser user) {
		return ResponseEntity.ok(GeneralUtils.buildUserInfo(user));
	}

	@GetMapping("/all")
	public ResponseEntity<?> getContent() {
		return ResponseEntity.ok("Public content goes here");
	}

	@GetMapping("/user")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getUserContent() {
		return ResponseEntity.ok("User content goes here");
	}
	@GetMapping("/friends/{id}")
	@PreAuthorize("hasRole('USER')")
	public Collection<Friend> getFriends(@PathVariable long id) {
		return userService.getUsersFriends(id);
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAdminContent() {
		return ResponseEntity.ok("Admin content goes here");
	}

	@GetMapping("/mod")
	@PreAuthorize("hasRole('MODERATOR')")
	public ResponseEntity<?> getModeratorContent() {
		return ResponseEntity.ok("Moderator content goes here");
	}
}
