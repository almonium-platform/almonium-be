package com.almonium.user.friendship.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.friendship.dto.FriendDto;
import com.almonium.user.friendship.dto.FriendshipRequestDto;
import com.almonium.user.friendship.model.entity.Friendship;
import com.almonium.user.friendship.model.enums.FriendshipAction;
import com.almonium.user.friendship.service.FriendshipService;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<FriendDto>> getMyFriends(@Auth Long id) {
        List<FriendDto> friends = friendshipService.getFriends(id);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/search")
    public ResponseEntity<FriendDto> searchFriendsByEmail(@RequestParam String email) {
        return friendshipService
                .findFriendByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Friendship> manageFriendship(
            @Auth User user, @PathVariable Long id, @Valid @RequestBody FriendshipAction action) {
        Friendship friendship = friendshipService.manageFriendship(user, id, action);
        return ResponseEntity.ok(friendship);
    }

    @PostMapping
    public ResponseEntity<Friendship> createFriendshipRequest(
            @Auth User user, @Valid @RequestBody FriendshipRequestDto dto) {
        Friendship friendship = friendshipService.createFriendshipRequest(user, dto);
        return ResponseEntity.ok(friendship);
    }
}
