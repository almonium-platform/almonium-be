package com.linguarium.friendship.controller;

import static lombok.AccessLevel.PRIVATE;

import com.linguarium.auth.annotation.CurrentUser;
import com.linguarium.friendship.dto.FriendDto;
import com.linguarium.friendship.dto.FriendshipRequestDto;
import com.linguarium.friendship.model.Friendship;
import com.linguarium.friendship.model.enums.FriendshipAction;
import com.linguarium.friendship.service.FriendshipService;
import com.linguarium.user.model.User;
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
@FieldDefaults(level = PRIVATE, makeFinal = true) // todo accessLevel.PRIVATE
public class FriendshipController {
    FriendshipService friendshipService;

    @GetMapping
    public ResponseEntity<List<FriendDto>> getMyFriends(@CurrentUser User user) {
        List<FriendDto> friends = friendshipService.getFriends(user.getId());
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
            @CurrentUser User user, @PathVariable Long id, @Valid @RequestBody FriendshipAction action) {
        Friendship friendship = friendshipService.manageFriendship(user, id, action);
        return ResponseEntity.ok(friendship);
    }

    @PostMapping
    public ResponseEntity<Friendship> createFriendshipRequest(
            @CurrentUser User user, @Valid @RequestBody FriendshipRequestDto dto) {
        Friendship friendship = friendshipService.createFriendshipRequest(user, dto);
        return ResponseEntity.ok(friendship);
    }
}
