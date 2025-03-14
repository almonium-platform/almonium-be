package com.almonium.user.core.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.user.core.dto.response.BaseProfileInfo;
import com.almonium.user.core.dto.response.UsernameAvailability;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User & Profile")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @GetMapping("/{username}/availability")
    public ResponseEntity<UsernameAvailability> checkUsernameAvailability(@PathVariable String username) {
        boolean isAvailable = userService.isUsernameAvailable(username);
        return ResponseEntity.ok(new UsernameAvailability(isAvailable));
    }

    @PostMapping("/block/{id}")
    public ResponseEntity<BaseProfileInfo> blockUser(@Auth User user, @PathVariable UUID id) {
        return ResponseEntity.ok(userService.blockUser(user, id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseProfileInfo> getUserProfile(@PathVariable UUID id, @Auth UUID viewer) {
        return ResponseEntity.ok(userService.getUserProfileInfo(viewer, id));
    }
}
