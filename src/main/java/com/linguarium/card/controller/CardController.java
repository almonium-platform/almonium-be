package com.linguarium.card.controller;

import static lombok.AccessLevel.PRIVATE;

import com.linguarium.auth.annotation.CurrentUser;
import com.linguarium.card.dto.CardCreationDto;
import com.linguarium.card.dto.CardDto;
import com.linguarium.card.dto.CardUpdateDto;
import com.linguarium.card.service.CardService;
import com.linguarium.user.model.User;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CardController {
    CardService cardService;

    @PostMapping
    public ResponseEntity<Void> createCard(@Valid @RequestBody CardCreationDto dto, @CurrentUser User user) {
        cardService.createCard(user.getLearner(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCard(
            @PathVariable Long id, @Valid @RequestBody CardUpdateDto dto, @CurrentUser User user) {
        cardService.updateCard(id, dto, user.getLearner());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CardDto>> getCardStack(@CurrentUser User user) {
        return ResponseEntity.ok(cardService.getUsersCards(user.getLearner()));
    }

    @GetMapping("/lang/{code}")
    public ResponseEntity<List<CardDto>> getCardStackOfLang(@PathVariable String code, @CurrentUser User user) {
        return ResponseEntity.ok(cardService.getUsersCardsOfLang(code, user.getLearner()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardDto> getCard(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.getCardById(id));
    }

    @GetMapping("/public/{hash}")
    public ResponseEntity<CardDto> getCardByHash(@PathVariable String hash) {
        return ResponseEntity.ok(cardService.getCardByPublicId(hash));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
