package com.almonium.user.core.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.user.core.dto.AvatarUrlDto;
import com.almonium.user.core.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ProfileController {
    ProfileService profileService;

    @PatchMapping("/avatar")
    public ResponseEntity<?> updateAvatar(@Auth Long id, @Valid @RequestBody AvatarUrlDto avatarDto) {
        profileService.updateAvatar(id, avatarDto.url());
        return ResponseEntity.ok().build();
    }
}
