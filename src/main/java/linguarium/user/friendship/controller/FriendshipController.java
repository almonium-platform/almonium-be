package linguarium.user.friendship.controller;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.Valid;
import java.util.List;
import linguarium.user.core.model.entity.User;
import linguarium.user.friendship.dto.FriendDto;
import linguarium.user.friendship.dto.FriendshipRequestDto;
import linguarium.user.friendship.model.entity.Friendship;
import linguarium.user.friendship.model.enums.FriendshipAction;
import linguarium.user.friendship.service.FriendshipService;
import linguarium.util.annotation.CurrentUser;
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
