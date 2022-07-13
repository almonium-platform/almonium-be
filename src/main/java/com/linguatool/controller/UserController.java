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
import org.springframework.web.bind.annotation.DeleteMapping;
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
        return ResponseEntity.ok(userService.buildUserInfo(user));
    }

    @DeleteMapping("/user/delete")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteAccount(@CurrentUser LocalUser localUser) {
        userService.deleteAccount(localUser.getUser());
        return ResponseEntity.ok("Successfully deleted");
    }

}
