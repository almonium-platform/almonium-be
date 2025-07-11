package com.almonium.user.core.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.user.core.dto.request.ProfileHiddenRequest;
import com.almonium.user.core.dto.response.BaseProfileInfo;
import com.almonium.user.core.service.ProfileInfoService;
import com.almonium.user.core.service.ProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User & Profile", description = "User profile management")
@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ProfileController {
    ProfileService profileService;
    ProfileInfoService profileInfoService;

    @PatchMapping("/ui-preferences")
    public ResponseEntity<Void> updateUIPreferences(@RequestBody Map<String, Object> uiPreferences, @Auth UUID userId) {
        profileService.updateUIPreferences(userId, uiPreferences);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/hidden")
    public ResponseEntity<Void> updateHidden(@RequestBody ProfileHiddenRequest request, @Auth UUID userId) {
        profileService.updateHidden(userId, request.hidden());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseProfileInfo> getUserProfile(@PathVariable UUID id, @Auth UUID viewer) {
        return ResponseEntity.ok(profileInfoService.getUserProfileInfo(viewer, id));
    }
}
