package com.almonium.subscription.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.auth.common.model.entity.Principal;
import com.almonium.subscription.dto.SessionResponseDto;
import com.almonium.subscription.service.PlanSubscriptionService;
import com.almonium.util.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class PlanSubscriptionController {
    PlanSubscriptionService planSubscriptionService;

    @PostMapping("/{id}")
    public ResponseEntity<SessionResponseDto> buySubscription(@PathVariable long id, @Auth Principal principal) {
        String sessionUrl = planSubscriptionService.initiatePlanSubscribing(principal.getUser(), id);
        return ResponseEntity.ok(new SessionResponseDto(sessionUrl));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> cancelMySubscription(@Auth Principal principal) {
        planSubscriptionService.initiateSubscriptionCancellation(principal.getUser());
        return ResponseEntity.ok(new ApiResponse(true, "Subscription cancelled successfully"));
    }
}
