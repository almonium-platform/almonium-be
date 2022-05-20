package com.linguatool.controller;

import com.linguatool.model.dto.FriendshipCommandDto;
import com.linguatool.model.FriendInfo;
import com.linguatool.service.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/friend")
public class FriendController {

    final UserServiceImpl userService;

    public FriendController(UserServiceImpl userService) {
        this.userService = userService;
    }


    @GetMapping("/friends/{id}")
    @PreAuthorize("hasRole('USER')")
    public Collection<FriendInfo> getFriends(@PathVariable long id) {
        return userService.getUsersFriends(id);
    }

    @GetMapping("/search/{email}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getFriends(@PathVariable String email) {
        Optional<FriendInfo> friendInfoOptional = userService.findFriendByEmail(email);
        return friendInfoOptional.isPresent() ? ResponseEntity.ok(friendInfoOptional.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping("/friendship")
    @PreAuthorize("hasRole('USER')")
    public void editFriendship(@Valid @RequestBody FriendshipCommandDto dto) {
        userService.editFriendship(dto);
    }
}
