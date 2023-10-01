package com.linguarium.card.controller;

import com.linguarium.auth.annotation.CurrentUser;
import com.linguarium.auth.model.LocalUser;
import com.linguarium.card.dto.CardCreationDto;
import com.linguarium.card.dto.CardDto;
import com.linguarium.card.dto.CardUpdateDto;
import com.linguarium.card.service.CardService;
import com.linguarium.suggestion.service.CardSuggestionService;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("api/cards")
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CardController {
    CardService cardService;
    private final CardSuggestionService cardSuggestionService;

    public CardController(CardService cardService, CardSuggestionService cardSuggestionService) {
        this.cardService = cardService;
        this.cardSuggestionService = cardSuggestionService;
    }

    @PostMapping("create")
    public ResponseEntity<?> createCard(@Valid @RequestBody CardCreationDto dto, @CurrentUser LocalUser userDetails) {
        cardService.createCard(userDetails.getUser().getLearner(), dto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("update")
    public ResponseEntity<?> updateCard(@Valid @RequestBody CardUpdateDto dto, @CurrentUser LocalUser user) {
        cardService.updateCard(dto, user.getUser().getLearner());
        return ResponseEntity.ok().build();
    }

    @GetMapping("all")
    public ResponseEntity<List<CardDto>> getCardStack(@CurrentUser LocalUser user) {
        return ResponseEntity.ok(cardService.getUsersCards(user.getUser().getLearner()));
    }

    @GetMapping("all/{code}")
    public ResponseEntity<List<CardDto>> getCardStackOfLang(@PathVariable String code, @CurrentUser LocalUser user) {
        return ResponseEntity.ok(cardService.getUsersCardsOfLang(code, user.getUser().getLearner()));
    }

    @GetMapping("suggested")
    public ResponseEntity<List<CardDto>> getSuggestedCardStack(@CurrentUser LocalUser user) {
        return ResponseEntity.ok(cardSuggestionService.getSuggestedCards(user.getUser()));
    }

    @GetMapping("{id}")
    public ResponseEntity<CardDto> getCard(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.getCardById(id));
    }

    @GetMapping("hash/{hash}")
    public ResponseEntity<CardDto> getCardByHash(@PathVariable String hash) {
        return ResponseEntity.ok(cardService.getCardByPublicId(hash));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CardDto> deleteCard(@PathVariable Long id) {
        cardService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
