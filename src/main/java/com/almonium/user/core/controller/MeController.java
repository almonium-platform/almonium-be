package com.almonium.user.core.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.user.core.dto.request.FluentLanguagesUpdateRequest;
import com.almonium.user.core.dto.request.SaveInterestsRequest;
import com.almonium.user.core.dto.request.UsernameUpdateRequest;
import com.almonium.user.core.dto.response.UserInfo;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User & Profile")
@RestController
@RequestMapping("/users/me")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class MeController {
    UserService userService;

    @GetMapping
    public ResponseEntity<UserInfo> getCurrentUser(@Auth User user) {
        return ResponseEntity.ok(userService.buildUserInfoFromUser(user));
    }

    @PatchMapping("/username")
    public ResponseEntity<Void> updateUsername(@Valid @RequestBody UsernameUpdateRequest request, @Auth User user) {
        userService.changeUsernameById(request.username(), user.getId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/interests")
    public ResponseEntity<Void> saveInterests(@Auth User user, @Valid @RequestBody SaveInterestsRequest interests) {
        userService.updateInterests(user, interests.ids());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/langs/fluent")
    public ResponseEntity<Void> updateFluentLanguages(
            @Valid @RequestBody FluentLanguagesUpdateRequest request, @Auth User user) {
        userService.updateFluentLanguages(request.langCodes(), user);
        return ResponseEntity.noContent().build();
    }
}
