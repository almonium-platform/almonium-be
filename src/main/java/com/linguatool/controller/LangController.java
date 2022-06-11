package com.linguatool.controller;

import com.linguatool.configuration.CurrentUser;
import com.linguatool.model.dto.FriendshipCommandDto;
import com.linguatool.model.dto.FriendInfo;
import com.linguatool.model.dto.LocalUser;
import com.linguatool.model.dto.api.request.CardCreationDto;
import com.linguatool.service.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/api/lang")
public class LangController {

    final UserServiceImpl userService;

    public LangController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/friends/{id}")
    @PreAuthorize("hasRole('USER')")
    public Collection<FriendInfo> getFriends(@PathVariable long id) {
        return userService.getUsersFriends(id);
    }

    @CrossOrigin
    @GetMapping("/search/{text}")
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> search(@PathVariable String text, @CurrentUser LocalUser userDetails) {
        System.out.println(userDetails.getUser().getCards());
        return ResponseEntity.ok("GOOD " + text);
    }

    @PostMapping("/friendship")
    @PreAuthorize("hasRole('USER')")
    public void editFriendship(@Valid @RequestBody FriendshipCommandDto dto) {
        userService.editFriendship(dto);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createCard(@Valid @RequestBody CardCreationDto dto, @CurrentUser LocalUser userDetails) {
        userService.createCard(userDetails.getUser(), dto);
        return ResponseEntity.ok().build();
    }
}
