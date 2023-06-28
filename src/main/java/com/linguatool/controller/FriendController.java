package com.linguatool.controller;

import com.linguatool.annotation.CurrentUser;
import com.linguatool.model.dto.*;
import com.linguatool.service.CardService;
import com.linguatool.service.FriendshipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/friend/")
public class FriendController {
    private final CardService cardService;
    private final FriendshipService friendshipService;

    public FriendController(CardService cardService, FriendshipService friendshipService) {
        this.cardService = cardService;
        this.friendshipService = friendshipService;
    }

    @GetMapping("friends/")
    public Collection<FriendInfo> getMyFriends(@CurrentUser LocalUser user) {
        return friendshipService.getUsersFriends(user.getUser().getId());
    }

    @GetMapping("search/{email}")
    public ResponseEntity<?> searchFriendsByEmail(@PathVariable String email) {
        Optional<FriendInfo> friendInfoOptional = friendshipService.findFriendByEmail(email);
        return friendInfoOptional.isPresent() ? ResponseEntity.ok(friendInfoOptional.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping("suggest/")
    public ResponseEntity<?> suggestCard(@RequestBody CardSuggestionDto dto, @CurrentUser LocalUser user) {
        boolean result = cardService.suggestCard(dto, user.getUser());
        if (result) return ResponseEntity.ok().build();
        else return ResponseEntity.badRequest().body("You've already sent that card!");
    }

    @PostMapping("accept/")
    public ResponseEntity<?> acceptCard(@RequestBody CardAcceptanceDto dto, @CurrentUser LocalUser user) {
        cardService.acceptSuggestion(dto, user.getUser());
        return ResponseEntity.ok().build();
    }

    @PostMapping("decline/")
    public ResponseEntity<?> declineCard(@RequestBody CardAcceptanceDto dto, @CurrentUser LocalUser user) {
        cardService.declineSuggestion(dto, user.getUser());
        return ResponseEntity.ok().build();
    }

    @PostMapping("friendship")
    public void editFriendship(@Valid @RequestBody FriendshipCommandDto dto) {
        friendshipService.editFriendship(dto);
    }
}
