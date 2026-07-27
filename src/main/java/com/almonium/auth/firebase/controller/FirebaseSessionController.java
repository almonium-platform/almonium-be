package com.almonium.auth.firebase.controller;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.auth.common.annotation.RequireRecentLogin;
import com.almonium.auth.firebase.dto.FirebaseSessionRequest;
import com.almonium.auth.firebase.model.FirebaseAuthProvider;
import com.almonium.auth.firebase.security.FirebasePrincipal;
import com.almonium.auth.firebase.service.FirebaseSessionCookieService;
import com.almonium.auth.firebase.service.FirebaseSessionService;
import com.almonium.config.properties.AppProperties;
import com.almonium.user.core.dto.response.UserInfo;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/session")
@RequiredArgsConstructor
public class FirebaseSessionController {
    private final FirebaseSessionService firebaseSessionService;
    private final FirebaseSessionCookieService cookieService;
    private final UserService userService;
    private final AppProperties appProperties;

    @PostMapping
    public ResponseEntity<UserInfo> createSession(
            @Valid @RequestBody FirebaseSessionRequest request, HttpServletResponse response) {
        User user = firebaseSessionService.createSession(request.idToken(), response);
        return ResponseEntity.ok(userService.buildUserInfoFromUser(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        cookieService.clear(response);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/providers")
    public ResponseEntity<List<FirebaseAuthProvider>> providers(@Auth FirebasePrincipal principal) {
        return ResponseEntity.ok(firebaseSessionService.getAuthProviders(principal.firebaseUid()));
    }

    @RequireRecentLogin
    @GetMapping("/recent")
    public ResponseEntity<RecentSessionResponse> recent(@Auth FirebasePrincipal principal) {
        Instant expiresAt = principal
                .authenticatedAt()
                .plusSeconds(appProperties.getAuth().getFirebase().getRecentLoginSeconds());
        return ResponseEntity.ok(new RecentSessionResponse(true, expiresAt.toString()));
    }

    public record RecentSessionResponse(boolean success, String message) {}
}
