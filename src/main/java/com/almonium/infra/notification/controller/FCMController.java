package com.almonium.infra.notification.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.infra.notification.dto.request.FCMTokenRequest;
import com.almonium.infra.notification.service.FCMService;
import com.almonium.user.core.model.entity.User;
import com.almonium.util.dto.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Infra")
@RestController
@RequestMapping("/fcm")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FCMController {
    FCMService fcmService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerToken(@RequestBody @Valid FCMTokenRequest request, @Auth User user) {
        fcmService.registerToken(request, user);
        return ResponseEntity.ok(new ApiResponse(true, "Token registered successfully"));
    }
}
