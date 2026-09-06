package com.almonium.user.core.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.user.core.dto.request.AvatarUrlDto;
import com.almonium.user.core.service.AvatarService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User & Profile")
@RestController
@RequestMapping("/profiles/me/avatars")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AvatarController {
    AvatarService avatarService;

    @PatchMapping("/default")
    public ResponseEntity<Void> chooseDefaultAvatar(@Auth UUID id, @Valid @RequestBody AvatarUrlDto avatarDto) {
        avatarService.chooseDefaultAvatar(id, avatarDto.avatarUrl());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/current")
    public ResponseEntity<Void> resetCurrentAvatar(@Auth UUID id) {
        avatarService.resetCurrentAvatar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
