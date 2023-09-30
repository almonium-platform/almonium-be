package com.linguarium.friendship.controller;

import com.linguarium.auth.annotation.CurrentUser;
import com.linguarium.auth.model.LocalUser;
import com.linguarium.friendship.dto.FriendshipActionDto;
import com.linguarium.friendship.dto.FriendInfoDto;
import com.linguarium.friendship.model.Friendship;
import com.linguarium.friendship.service.impl.FriendshipServiceImpl;
import com.linguarium.suggestion.dto.CardAcceptanceDto;
import com.linguarium.suggestion.dto.CardSuggestionDto;
import com.linguarium.suggestion.service.CardSuggestionService;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Collection;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/api/friend/")
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FriendController {
    CardSuggestionService cardSuggestionService;
    FriendshipServiceImpl friendshipServiceImpl;

    public FriendController(CardSuggestionService cardSuggestionService, FriendshipServiceImpl friendshipServiceImpl) {
        this.cardSuggestionService = cardSuggestionService;
        this.friendshipServiceImpl = friendshipServiceImpl;
    }

    @GetMapping("friends/")
    public Collection<FriendInfoDto> getMyFriends(@CurrentUser LocalUser user) {
        return friendshipServiceImpl.getFriends(user.getUser().getId());
    }

    @GetMapping("search/{email}")
    public ResponseEntity<?> searchFriendsByEmail(@PathVariable String email) {
        Optional<FriendInfoDto> friendInfoOptional = friendshipServiceImpl.findFriendByEmail(email);
        return friendInfoOptional.isPresent() ? ResponseEntity.ok(friendInfoOptional.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping("suggest/")
    public ResponseEntity<?> suggestCard(@RequestBody CardSuggestionDto dto, @CurrentUser LocalUser user) {
        boolean result = cardSuggestionService.suggestCard(dto, user.getUser());
        if (result) return ResponseEntity.ok().build();
        else return ResponseEntity.badRequest().body("You've already sent that card!");
    }

    @PostMapping("accept/")
    public ResponseEntity<?> acceptCard(@RequestBody CardAcceptanceDto dto, @CurrentUser LocalUser user) {
        cardSuggestionService.acceptSuggestion(dto, user.getUser());
        return ResponseEntity.ok().build();
    }

    @PostMapping("decline/")
    public ResponseEntity<?> declineCard(@RequestBody CardAcceptanceDto dto, @CurrentUser LocalUser user) {
        cardSuggestionService.declineSuggestion(dto, user.getUser());
        return ResponseEntity.ok().build();
    }

    @PostMapping("friendship")
    public ResponseEntity<?> editFriendship(@Valid @RequestBody FriendshipActionDto dto) {
        Friendship friendship = friendshipServiceImpl.editFriendship(dto);
        return new ResponseEntity<>(friendship, HttpStatus.OK);
    }
}
