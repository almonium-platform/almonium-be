package com.linguarium.friendship.controller;

import static lombok.AccessLevel.PRIVATE;

import com.linguarium.auth.annotation.CurrentUser;
import com.linguarium.friendship.dto.FriendshipActionDto;
import com.linguarium.friendship.dto.FriendshipInfoDto;
import com.linguarium.friendship.model.Friendship;
import com.linguarium.friendship.service.FriendshipService;
import com.linguarium.user.model.User;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FriendshipController {
    FriendshipService friendshipService;

    @GetMapping
    public ResponseEntity<List<FriendshipInfoDto>> getMyFriends(@CurrentUser User user) {
        List<FriendshipInfoDto> friends = friendshipService.getFriends(user.getId());
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/search")
    public ResponseEntity<FriendshipInfoDto> searchFriendsByEmail(@RequestParam String email) {
        return friendshipService
                .findFriendByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/friendships")
    public ResponseEntity<Friendship> manageFriendship(@Valid @RequestBody FriendshipActionDto dto) {
        Friendship friendship = friendshipService.manageFriendship(dto);
        return ResponseEntity.ok(friendship);
    }
}
