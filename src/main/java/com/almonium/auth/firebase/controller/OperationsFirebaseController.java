package com.almonium.auth.firebase.controller;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.auth.common.annotation.RequireRecentLogin;
import com.almonium.auth.firebase.dto.request.FirebasePurgeRequest;
import com.almonium.auth.firebase.gateway.FirebaseAuthGateway;
import com.almonium.auth.firebase.model.FirebaseAccountSummary;
import com.almonium.auth.firebase.service.FirebaseUserPurgeService;
import com.almonium.user.core.model.entity.User;
import com.almonium.util.dto.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Emptying the Firebase user directory, which a database drop leaves untouched. Deliberately not
 * reachable without signing in, which is the same reason the admin claim is set by a script: there
 * is no way to bootstrap out of an empty Firebase project from inside the app.
 */
@RestController
@RequestMapping("/ops/firebase/users")
@RequiredArgsConstructor
public class OperationsFirebaseController {
    private final FirebaseUserPurgeService purgeService;
    private final FirebaseAuthGateway firebaseAuthGateway;

    /**
     * What Firebase itself believes about an account. Worth having, because our row and Firebase can
     * disagree for months without anything noticing.
     */
    @GetMapping
    public ResponseEntity<FirebaseAccountSummary> findByEmail(@RequestParam String email) {
        return ResponseEntity.ok(firebaseAuthGateway
                .findAccountByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("No Firebase account with email: " + email)));
    }

    /** What the purge will demand, and how many accounts it is looking at. */
    @GetMapping("/purge")
    public ResponseEntity<Map<String, Object>> purgeConfirmation() {
        return ResponseEntity.ok(
                Map.of("confirmation", purgeService.confirmationPhrase(), "userCount", purgeService.countUsers()));
    }

    @PostMapping("/purge")
    @RequireRecentLogin
    public ResponseEntity<ApiResponse> purge(@Valid @RequestBody FirebasePurgeRequest request, @Auth User operator) {
        String expected = purgeService.confirmationPhrase();
        if (!expected.equals(request.confirmation())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Confirmation must read exactly: " + expected));
        }

        int deleted = purgeService.purge(operator.getFirebaseUid(), request.includeOperator());
        return ResponseEntity.ok(new ApiResponse(
                true,
                String.format(
                        "Deleted %d Firebase %s%s",
                        deleted,
                        deleted == 1 ? "account" : "accounts",
                        request.includeOperator() ? ", including your own" : ", sparing your own")));
    }
}
