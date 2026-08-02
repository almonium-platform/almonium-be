package com.almonium.learning.book.controller;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.auth.common.annotation.RequireRecentLogin;
import com.almonium.learning.book.dto.request.QuotaResetRequest;
import com.almonium.learning.book.service.UserBookImportService;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.UserService;
import com.almonium.util.dto.ApiResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ops/users/{userId}/book-import-quota")
@RequiredArgsConstructor
public class OperationsBookImportController {
    private final UserBookImportService userBookImportService;
    private final UserService userService;

    @PostMapping("/reset")
    @RequireRecentLogin
    public ResponseEntity<ApiResponse> reset(
            @PathVariable UUID userId, @Valid @RequestBody QuotaResetRequest request, @Auth User operator) {
        userBookImportService.resetCurrentPeriodQuota(userService.getById(userId), operator, request.reason());
        return ResponseEntity.ok(new ApiResponse(true, "Book-import quota reset"));
    }
}
