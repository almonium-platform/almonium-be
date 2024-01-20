package com.linguarium.friendship.controller;

import com.linguarium.auth.annotation.CurrentUser;
import com.linguarium.auth.model.LocalUser;
import com.linguarium.friendship.dto.FriendInfoDto;
import com.linguarium.friendship.dto.FriendshipActionDto;
import com.linguarium.friendship.model.Friendship;
import com.linguarium.friendship.service.FriendshipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FriendController {
    FriendshipService friendshipService;

    @GetMapping
    public ResponseEntity<List<FriendInfoDto>> getMyFriends(@CurrentUser LocalUser user) {
        List<FriendInfoDto> friends = friendshipService.getFriends(user.getUser().getId());
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/search")
    public ResponseEntity<FriendInfoDto> searchFriendsByEmail(@RequestParam String email) {
        return friendshipService.findFriendByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/friendships")
    public ResponseEntity<Friendship> manageFriendship(@Valid @RequestBody FriendshipActionDto dto) {
        Friendship friendship = friendshipService.manageFriendship(dto);
        return ResponseEntity.ok(friendship);
    }
}
