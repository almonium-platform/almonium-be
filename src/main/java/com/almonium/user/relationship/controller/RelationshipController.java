package com.almonium.user.relationship.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.subscription.constant.AppLimits;
import com.almonium.user.core.dto.response.BaseProfileInfo;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.RelationshipActionsFacade;
import com.almonium.user.relationship.dto.request.FriendshipRequestDto;
import com.almonium.user.relationship.dto.request.RelationshipActionDto;
import com.almonium.user.relationship.dto.response.PublicUserProfile;
import com.almonium.user.relationship.dto.response.RelatedUserProfile;
import com.almonium.user.relationship.model.projection.RelationshipToUserProjection;
import com.almonium.user.relationship.service.RelationshipService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User & Profile")
@RestController
@RequestMapping("/relationships")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RelationshipController {
    RelationshipService relationshipService;
    RelationshipActionsFacade relationshipActionsFacade;

    @GetMapping
    public ResponseEntity<List<RelatedUserProfile>> getMyFriends(@Auth UUID id) {
        return ResponseEntity.ok(relationshipService.getFriends(id));
    }

    @GetMapping("/blocked")
    public ResponseEntity<List<RelatedUserProfile>> getBlocked(@Auth UUID id) {
        return ResponseEntity.ok(relationshipService.getBlocked(id));
    }

    @GetMapping("/requests/sent")
    public ResponseEntity<List<RelatedUserProfile>> getSentRequests(@Auth UUID id) {
        return ResponseEntity.ok(relationshipService.getSentRequests(id));
    }

    @GetMapping("/requests/received")
    public ResponseEntity<List<RelatedUserProfile>> getReceivedRequests(@Auth UUID id) {
        return ResponseEntity.ok(relationshipService.getReceivedRequests(id));
    }

    // you won't be able to find people who you've blocked or who have blocked you
    @GetMapping("/search/all")
    public ResponseEntity<List<PublicUserProfile>> searchUsersByUsername(
            @RequestParam @Size(min = AppLimits.MIN_USERNAME_LENGTH, max = AppLimits.MAX_USERNAME_LENGTH)
                    String username,
            @Auth UUID id) {
        return ResponseEntity.ok(relationshipService.findUsersByUsername(id, username));
    }

    // to be used in the future
    @GetMapping("/search/friends")
    public ResponseEntity<List<RelationshipToUserProjection>> searchFriends(
            @RequestParam @Size(min = AppLimits.MIN_USERNAME_LENGTH, max = AppLimits.MAX_USERNAME_LENGTH)
                    String username,
            @Auth UUID id) {
        return ResponseEntity.ok(relationshipService.searchFriends(id, username));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseProfileInfo> manageFriendship(
            @Auth User user, @PathVariable UUID id, @Valid @RequestBody RelationshipActionDto dto) {
        return ResponseEntity.ok(relationshipActionsFacade.manageFriendship(user, id, dto.action()));
    }

    @PostMapping("/block/{id}")
    public ResponseEntity<BaseProfileInfo> blockUser(@Auth User user, @PathVariable UUID id) {
        return ResponseEntity.ok(relationshipActionsFacade.blockUser(user, id));
    }

    @PostMapping
    public ResponseEntity<BaseProfileInfo> createFriendshipRequest(
            @Auth User user, @Valid @RequestBody FriendshipRequestDto dto) {
        return ResponseEntity.ok(relationshipActionsFacade.createFriendshipRequest(user, dto));
    }
}
