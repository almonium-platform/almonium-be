package com.almonium.user.core.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.annotation.Auth;
import com.almonium.user.core.dto.LanguageSetupRequest;
import com.almonium.user.core.dto.LanguageUpdateRequest;
import com.almonium.user.core.dto.UserInfo;
import com.almonium.user.core.dto.UsernameAvailability;
import com.almonium.user.core.dto.UsernameUpdateRequest;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.LearnerService;
import com.almonium.user.core.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    LearnerService learnerService;

    @GetMapping("/me")
    public ResponseEntity<UserInfo> getCurrentUser(@Auth User user) {
        return ResponseEntity.ok(userService.buildUserInfoFromUser(user));
    }

    @GetMapping("/{username}/availability")
    public ResponseEntity<UsernameAvailability> checkUsernameAvailability(@PathVariable String username) {
        boolean isAvailable = userService.isUsernameAvailable(username);
        return ResponseEntity.ok(new UsernameAvailability(isAvailable));
    }

    @PatchMapping("/me/username")
    public ResponseEntity<Void> updateUsername(@RequestBody UsernameUpdateRequest request, @Auth User user) {
        userService.changeUsernameById(request.username(), user.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me/langs")
    public ResponseEntity<Void> setupLanguages(@RequestBody LanguageSetupRequest request, @Auth User user) {
        learnerService.setupLanguages(request.fluentLangs(), request.targetLangs(), user.getLearner());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/me/langs/target/{code}")
    public ResponseEntity<Void> addTargetLanguage(@PathVariable Language code, @Auth User user) {
        learnerService.addTargetLanguage(code, user.getLearner().getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me/langs/target/{code}")
    public ResponseEntity<Void> removeTargetLanguage(@PathVariable Language code, @Auth User user) {
        learnerService.removeTargetLanguage(code, user.getLearner().getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me/langs/fluent")
    public ResponseEntity<Void> updateFluentLanguages(
            @Valid @RequestBody LanguageUpdateRequest request, @Auth User user) {
        learnerService.updateFluentLanguages(request.langCodes(), user.getLearner());
        return ResponseEntity.noContent().build();
    }
}
