package com.linguatool.controller;

import com.linguatool.configuration.CurrentUser;
import com.linguatool.model.dto.*;
import com.linguatool.service.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/friend/")
public class FriendController {

    final UserServiceImpl userService;

    public FriendController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("friends/")
    public Collection<FriendInfo> getMyFriends(@CurrentUser LocalUser user) {
        return userService.getUsersFriends(user.getUser().getId());
    }

    @GetMapping("search/{email}")
    public ResponseEntity<?> searchFriendsByEmail(@PathVariable String email) {
        Optional<FriendInfo> friendInfoOptional = userService.findFriendByEmail(email);
        return friendInfoOptional.isPresent() ? ResponseEntity.ok(friendInfoOptional.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping("suggest/")
    public ResponseEntity<?> suggestCard(@RequestBody CardSuggestionDto dto, @CurrentUser LocalUser user) {
        boolean result = userService.suggestCard(dto, user.getUser());
        if (result) return ResponseEntity.ok().build();
        else return ResponseEntity.badRequest().body("You've already sent that card!");
    }

    @PostMapping("accept/")
    public ResponseEntity<?> acceptCard(@RequestBody CardAcceptanceDto dto, @CurrentUser LocalUser user) {
        userService.acceptSuggestion(dto, user.getUser());
        return ResponseEntity.ok().body("ok");
    }

    @PostMapping("reject/")
    public ResponseEntity<?> rejectCard(@RequestBody CardAcceptanceDto dto, @CurrentUser LocalUser user) {
        userService.rejectSuggestion(dto, user.getUser());
        return ResponseEntity.ok().body("ok");
    }

    @PostMapping("friendship")
    public void editFriendship(@Valid @RequestBody FriendshipCommandDto dto) {
        userService.editFriendship(dto);
    }
}
