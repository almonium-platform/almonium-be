package com.almonium.auth.common.controller.sensitive;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.auth.common.annotation.RequireRecentLogin;
import com.almonium.auth.common.dto.request.EmailRequestDto;
import com.almonium.auth.common.model.entity.Principal;
import com.almonium.auth.common.service.SensitiveAuthActionsService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/email/change-request")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequireRecentLogin
public class EmailChangeRequestController {
    SensitiveAuthActionsService sensitiveAuthActionsService;

    @PostMapping
    public ResponseEntity<?> requestEmailChange(@Auth Principal auth, @RequestBody EmailRequestDto request) {
        sensitiveAuthActionsService.requestEmailChange(auth.getUser().getId(), request.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendEmailChangeRequest(@Auth Principal auth) {
        sensitiveAuthActionsService.resendEmailChangeRequest(auth.getUser().getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteEmailChangeRequest(@Auth Principal auth) {
        sensitiveAuthActionsService.cancelEmailChangeRequest(auth.getUser().getId());
        return ResponseEntity.ok().build();
    }
}
