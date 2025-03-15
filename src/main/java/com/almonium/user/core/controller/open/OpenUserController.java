package com.almonium.user.core.controller.open;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.core.dto.response.BaseProfileInfo;
import com.almonium.user.core.service.ProfileInfoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User & Profile")
@RestController
@RequestMapping("/public/profiles")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class OpenUserController {
    ProfileInfoService profileInfoService;

    @GetMapping("/{id}")
    public ResponseEntity<BaseProfileInfo> getById(@NonNull @PathVariable UUID id) {
        return ResponseEntity.ok(profileInfoService.getPublicProfileInfo(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<BaseProfileInfo> getByUsername(@NotBlank @PathVariable String username) {
        return ResponseEntity.ok(profileInfoService.getPublicProfileInfoByUsername(username));
    }
}
