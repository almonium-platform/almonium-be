package com.almonium.subscription.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.subscription.dto.response.AnnualNudgeDto;
import com.almonium.subscription.dto.response.CadenceChangePreviewDto;
import com.almonium.subscription.dto.response.SessionResponseDto;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.enums.CadenceChangeOption;
import com.almonium.subscription.service.CadenceChangeService;
import com.almonium.subscription.service.PlanSubscriptionService;
import com.almonium.user.core.model.entity.User;
import com.almonium.util.dto.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User & Profile")
@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class PlanSubscriptionController {
    PlanSubscriptionService planSubscriptionService;
    CadenceChangeService cadenceChangeService;

    @PostMapping("/portal")
    public ResponseEntity<SessionResponseDto> accessCustomerPortal(@Auth User user) {
        return ResponseEntity.ok(new SessionResponseDto(planSubscriptionService.initiateCustomerPortalAccess(user)));
    }

    @PostMapping("/plans/{id}")
    public ResponseEntity<SessionResponseDto> buySubscription(
            @PathVariable Long id, @RequestParam(defaultValue = "false") boolean founder, @Auth User user) {
        String sessionUrl = planSubscriptionService.initiatePlanSubscribing(user, id, founder);
        return ResponseEntity.ok(new SessionResponseDto(sessionUrl));
    }

    @GetMapping("/cadence-change")
    public ResponseEntity<CadenceChangePreviewDto> previewCadenceChange(
            @RequestParam Plan.Type target, @Auth User user) {
        return ResponseEntity.ok(cadenceChangeService.preview(user, target));
    }

    @PostMapping("/cadence-change")
    public ResponseEntity<ApiResponse> changeCadence(
            @RequestParam Plan.Type target, @RequestParam CadenceChangeOption option, @Auth User user) {
        cadenceChangeService.apply(user, target, option);
        return ResponseEntity.ok(new ApiResponse(true, "Billing cadence updated"));
    }

    @GetMapping("/annual-nudge")
    public ResponseEntity<AnnualNudgeDto> annualNudge(@Auth User user) {
        return ResponseEntity.ok(cadenceChangeService.annualNudge(user));
    }

    @DeleteMapping("/cadence-change")
    public ResponseEntity<ApiResponse> undoCadenceChange(@Auth User user) {
        cadenceChangeService.undo(user);
        return ResponseEntity.ok(new ApiResponse(true, "Scheduled billing change cancelled"));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> downgradeMe(@Auth User user) {
        planSubscriptionService.downgradeMe(user);
        return ResponseEntity.ok(new ApiResponse(true, "Subscription cancelled successfully"));
    }
}
