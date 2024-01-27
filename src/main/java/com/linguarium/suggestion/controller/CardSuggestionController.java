package com.linguarium.suggestion.controller;

import com.linguarium.auth.annotation.CurrentUser;
import com.linguarium.auth.model.LocalUser;
import com.linguarium.card.dto.CardDto;
import com.linguarium.suggestion.dto.CardSuggestionDto;
import com.linguarium.suggestion.service.CardSuggestionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards/suggestions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CardSuggestionController {
    CardSuggestionService cardSuggestionService;

    @PostMapping
    public ResponseEntity<?> suggestCard(@Valid @RequestBody CardSuggestionDto dto, @CurrentUser LocalUser user) {
        cardSuggestionService.suggestCard(dto, user.getUser().getLearner());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<Void> acceptCard(@PathVariable Long id, @CurrentUser LocalUser user) {
        cardSuggestionService.acceptSuggestion(id, user.getUser().getLearner());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/decline")
    public ResponseEntity<Void> declineCard(@PathVariable Long id, @CurrentUser LocalUser user) {
        cardSuggestionService.declineSuggestion(id, user.getUser().getLearner());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CardDto>> getSuggestedCardStack(@CurrentUser LocalUser user) {
        return ResponseEntity.ok(cardSuggestionService.getSuggestedCards(user.getUser().getLearner()));
    }
}
