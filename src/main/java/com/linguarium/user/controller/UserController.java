package com.linguarium.user.controller;

import com.linguarium.auth.annotation.CurrentUser;
import com.linguarium.auth.model.LocalUser;
import com.linguarium.user.dto.LangCodeDto;
import com.linguarium.user.service.LearnerService;
import com.linguarium.user.service.UserService;
import com.linguarium.user.service.impl.UserServiceImpl;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/api/user/")
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserController {
     UserService userService;
     LearnerService learnerService;

    public UserController(UserServiceImpl userService, LearnerService learnerService) {
        this.userService = userService;
        this.learnerService = learnerService;
    }

    @GetMapping("me")
    public ResponseEntity<?> getCurrentUser(@CurrentUser LocalUser user) {
        return ResponseEntity.ok(userService.buildUserInfo(user));
    }

    @GetMapping("check/{username}")
    public ResponseEntity<Boolean> isUsernameAvailable(@PathVariable String username) {
        return ResponseEntity.ok(!userService.existsByUsername(username));
    }

    @PostMapping("change/{username}")
    public ResponseEntity<Boolean> changeUsername(@PathVariable String username, @CurrentUser LocalUser user) {
        userService.changeUsername(username, user.getUser().getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> deleteAccount(@CurrentUser LocalUser user) {
        userService.deleteAccount(user.getUser());
        return ResponseEntity.ok("{'text' : 'ok'}");
    }

    @PostMapping("target")
    public ResponseEntity<?> setTargetLangs(@RequestBody LangCodeDto dto, @CurrentUser LocalUser user) {
        learnerService.setTargetLangs(dto, user.getUser().getLearner());
        return ResponseEntity.ok().build();
    }

    @PostMapping("fluent")
    public ResponseEntity<?> setFluentLangs(@RequestBody LangCodeDto dto, @CurrentUser LocalUser user) {
        learnerService.setFluentLangs(dto, user.getUser().getLearner());
        return ResponseEntity.ok().build();
    }
}