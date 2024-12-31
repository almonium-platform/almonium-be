package com.almonium.user.core.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.annotation.Auth;
import com.almonium.user.core.dto.LanguageUpdateRequest;
import com.almonium.user.core.dto.TargetLanguageWithProficiency;
import com.almonium.user.core.dto.UserInfo;
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
@RequestMapping("/users/me")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class MeController {
    UserService userService;
    LearnerService learnerService;

    @GetMapping
    public ResponseEntity<UserInfo> getCurrentUser(@Auth User user) {
        return ResponseEntity.ok(userService.buildUserInfoFromUser(user));
    }

    @PatchMapping("/username")
    public ResponseEntity<Void> updateUsername(@RequestBody UsernameUpdateRequest request, @Auth User user) {
        userService.changeUsernameById(request.username(), user.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/langs/target")
    public ResponseEntity<Void> addTargetLanguage(
            @Valid @RequestBody TargetLanguageWithProficiency dto, @Auth User user) {
        learnerService.addTargetLanguage(dto, user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/langs/target/{code}")
    public ResponseEntity<Void> removeTargetLanguage(@PathVariable Language code, @Auth User user) {
        learnerService.removeTargetLanguage(code, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/langs/fluent")
    public ResponseEntity<Void> updateFluentLanguages(
            @Valid @RequestBody LanguageUpdateRequest request, @Auth User user) {
        userService.updateFluentLanguages(request.langCodes(), user);
        return ResponseEntity.noContent().build();
    }
}
