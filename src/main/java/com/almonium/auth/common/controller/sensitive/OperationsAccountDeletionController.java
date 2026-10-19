package com.almonium.auth.common.controller.sensitive;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.auth.common.annotation.RequireRecentLogin;
import com.almonium.auth.common.service.SensitiveAuthActionsService;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.UserService;
import com.almonium.util.dto.ApiResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Deletes someone else's account exactly as {@code DELETE /auth/me} would: same order, same paid-plan
 * cancellation, same farewell mail. Only the trigger differs.
 */
@RestController
@RequestMapping("/ops/users/{userId}")
@RequiredArgsConstructor
@RequireRecentLogin
public class OperationsAccountDeletionController {
    private final SensitiveAuthActionsService sensitiveAuthActionsService;
    private final UserService userService;

    @DeleteMapping
    public ResponseEntity<ApiResponse> delete(@PathVariable UUID userId, @Auth User operator) {
        // Your own account goes through /auth/me, which also clears the session cookie this request rides on.
        if (operator.getId().equals(userId)) {
            throw new BadUserRequestActionException("Delete your own account from your profile, not from ops");
        }
        sensitiveAuthActionsService.deleteAccount(userService.getById(userId));
        return ResponseEntity.ok(new ApiResponse(true, "Account deleted"));
    }
}
