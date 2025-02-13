package com.almonium.user.core.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.user.core.dto.AvatarDto;
import com.almonium.user.core.dto.AvatarUrlDto;
import com.almonium.user.core.service.AvatarService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profiles/me/avatars")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AvatarController {
    AvatarService avatarService;

    @PostMapping
    public ResponseEntity<Void> addAndSetNewCustomAvatar(@Auth Long id, @Valid @RequestBody AvatarUrlDto avatarDto) {
        avatarService.addAndSetNewCustomAvatar(id, avatarDto.avatarUrl());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<AvatarDto>> getAvatars(@Auth Long id) {
        return ResponseEntity.ok(avatarService.getAvatars(id));
    }

    @PatchMapping("/{avatarId}")
    public ResponseEntity<Void> chooseExistingCustomAvatar(@Auth Long id, @PathVariable Long avatarId) {
        avatarService.chooseExistingCustomAvatar(id, avatarId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/default")
    public ResponseEntity<Void> chooseDefaultAvatar(@Auth Long id, @Valid @RequestBody AvatarUrlDto avatarDto) {
        avatarService.chooseDefaultAvatar(id, avatarDto.avatarUrl());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{avatarId}")
    public ResponseEntity<Void> deleteCustomAvatar(@Auth Long id, @PathVariable Long avatarId) {
        avatarService.deleteCustomAvatar(id, avatarId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/current")
    public ResponseEntity<Void> resetCurrentAvatar(@Auth Long id) {
        avatarService.resetCurrentAvatar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
