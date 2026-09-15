package com.almonium.auth.common.controller.sensitive;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.auth.common.annotation.RequireRecentLogin;
import com.almonium.auth.common.service.SensitiveAuthActionsService;
import com.almonium.auth.firebase.service.FirebaseSessionCookieService;
import com.almonium.user.core.model.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@RequireRecentLogin
public class SensitiveAuthActionsController {
    private final SensitiveAuthActionsService sensitiveAuthActionsService;
    private final FirebaseSessionCookieService sessionCookieService;

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteCurrentUserAccount(@Auth User user, HttpServletResponse response) {
        sensitiveAuthActionsService.deleteAccount(user);
        sessionCookieService.clear(response);
        return ResponseEntity.noContent().build();
    }
}
