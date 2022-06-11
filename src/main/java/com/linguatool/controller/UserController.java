package com.linguatool.controller;

import com.linguatool.configuration.CurrentUser;
import com.linguatool.model.dto.FriendshipCommandDto;
import com.linguatool.model.dto.LocalUser;
import com.linguatool.model.dto.FriendInfo;
import com.linguatool.service.UserServiceImpl;
import com.linguatool.util.GeneralUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

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

    @CrossOrigin
    @GetMapping("/userok/{email}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserokContent(@PathVariable String email) {
        return ResponseEntity.ok("Userok content goes here");
    }

    @GetMapping("/friends/{id}")
    @PreAuthorize("hasRole('USER')")
    public Collection<FriendInfo> getFriends(@PathVariable long id) {
        return userService.getUsersFriends(id);
    }

    @GetMapping("/search/{email}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getFriends(@PathVariable String email) {
        Optional<FriendInfo> friendInfoOptional = userService.findFriendByEmail(email);
        return friendInfoOptional.isPresent() ? ResponseEntity.ok(friendInfoOptional.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping("/friendship")
    @PreAuthorize("hasRole('USER')")
    public void editFriendship(@Valid @RequestBody FriendshipCommandDto dto) {
        userService.editFriendship(dto);
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
