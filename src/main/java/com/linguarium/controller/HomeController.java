package com.linguarium.controller;

import com.linguarium.auth.annotation.CurrentUser;
import com.linguarium.auth.model.LocalUser;
import com.linguarium.card.dto.CardDto;
import com.linguarium.card.service.CardService;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/api/home")
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class HomeController {
    CardService cardService;

    public HomeController(CardService userService) {
        this.cardService = userService;
    }

    @GetMapping("/cards")
    public ResponseEntity<List<CardDto>> getCurrentUser(@CurrentUser LocalUser user) {
        return ResponseEntity.ok(cardService.getUsersCards(user.getUser().getLearner()));
    }
}
