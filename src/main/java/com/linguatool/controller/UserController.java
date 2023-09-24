package com.linguatool.controller;

import com.linguatool.annotation.CurrentUser;
import com.linguatool.model.dto.LangCodeDto;
import com.linguatool.model.dto.LocalUser;
import com.linguatool.repository.UserRepository;
import com.linguatool.service.impl.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/")
public class UserController {

    private final UserServiceImpl userService;
    private final UserRepository userRepository;

    public UserController(UserServiceImpl userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("me")
    public ResponseEntity<?> getCurrentUser(@CurrentUser LocalUser user) {
        return ResponseEntity.ok(userService.buildUserInfo(user));
    }

    @GetMapping("check/{username}")
    public ResponseEntity<Boolean> isUsernameAvailable(@PathVariable String username) {
        return ResponseEntity.ok(!userRepository.existsByUsername(username));
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
        userService.setTargetLangs(dto, user.getUser());
        return ResponseEntity.ok().build();
    }

    @PostMapping("fluent")
    public ResponseEntity<?> setFluentLangs(@RequestBody LangCodeDto dto, @CurrentUser LocalUser user) {
        userService.setFluentLangs(dto, user.getUser());
        return ResponseEntity.ok().build();
    }
}