package com.almonium.card.suggestion.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.annotation.Auth;
import com.almonium.card.core.dto.response.CardDto;
import com.almonium.card.suggestion.dto.request.CardSuggestionDto;
import com.almonium.card.suggestion.service.CardSuggestionService;
import com.almonium.user.core.model.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Learning")
@RestController
@RequestMapping("/cards/suggestions")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CardSuggestionController {
    CardSuggestionService cardSuggestionService;

    @PostMapping
    public ResponseEntity<Void> suggestCard(@Valid @RequestBody CardSuggestionDto dto, @Auth User user) {
        cardSuggestionService.suggestCard(dto, user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<Void> acceptCard(@PathVariable UUID id, @Auth User user) {
        cardSuggestionService.acceptSuggestion(id, user);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/decline")
    public ResponseEntity<Void> declineCard(@PathVariable UUID id, @Auth User user) {
        cardSuggestionService.declineSuggestion(id, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/lang/{lang}")
    public ResponseEntity<List<CardDto>> getSuggestedCardStack(@PathVariable Language lang, @Auth User user) {
        return ResponseEntity.ok(cardSuggestionService.getSuggestedCards(user, lang));
    }
}
