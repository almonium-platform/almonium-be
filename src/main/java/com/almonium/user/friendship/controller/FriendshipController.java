package com.almonium.user.friendship.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.subscription.constant.AppLimits;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.friendship.dto.request.FriendshipActionDto;
import com.almonium.user.friendship.dto.request.FriendshipRequestDto;
import com.almonium.user.friendship.dto.response.PublicUserProfile;
import com.almonium.user.friendship.dto.response.RelatedUserProfile;
import com.almonium.user.friendship.model.projection.FriendshipToUserProjection;
import com.almonium.user.friendship.service.FriendshipService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.List;
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

@RestController
@RequestMapping("/friendships")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FriendshipController {
    FriendshipService friendshipService;

    @GetMapping
    public ResponseEntity<List<RelatedUserProfile>> getMyFriends(@Auth Long id) {
        return ResponseEntity.ok(friendshipService.getFriends(id));
    }

    @GetMapping("/blocked")
    public ResponseEntity<List<RelatedUserProfile>> getBlocked(@Auth Long id) {
        return ResponseEntity.ok(friendshipService.getBlocked(id));
    }

    @GetMapping("/requests/sent")
    public ResponseEntity<List<RelatedUserProfile>> getSentRequests(@Auth Long id) {
        return ResponseEntity.ok(friendshipService.getSentRequests(id));
    }

    @GetMapping("/requests/received")
    public ResponseEntity<List<RelatedUserProfile>> getReceivedRequests(@Auth Long id) {
        return ResponseEntity.ok(friendshipService.getReceivedRequests(id));
    }

    @GetMapping("/search/all")
    public ResponseEntity<List<PublicUserProfile>> searchUsersByUsername(
            @RequestParam @Size(min = AppLimits.MIN_USERNAME_LENGTH, max = AppLimits.MAX_USERNAME_LENGTH)
                    String username,
            @Auth Long id) {
        return ResponseEntity.ok(friendshipService.findUsersByUsername(id, username));
    }

    // to be used in the future
    @GetMapping("/search/friends")
    public ResponseEntity<List<FriendshipToUserProjection>> searchFriends(
            @RequestParam @Size(min = AppLimits.MIN_USERNAME_LENGTH, max = AppLimits.MAX_USERNAME_LENGTH)
                    String username,
            @Auth Long id) {
        return ResponseEntity.ok(friendshipService.searchFriends(id, username));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> manageFriendship(
            @Auth User user, @PathVariable Long id, @Valid @RequestBody FriendshipActionDto dto) {
        friendshipService.manageFriendship(user, id, dto.action());
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> createFriendshipRequest(@Auth User user, @Valid @RequestBody FriendshipRequestDto dto) {
        friendshipService.createFriendshipRequest(user, dto);
        return ResponseEntity.ok().build();
    }
}
