package com.almonium.card.deck.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.card.deck.dto.request.DeckCreationRequest;
import com.almonium.card.deck.dto.request.DeckUpdateRequest;
import com.almonium.card.deck.dto.request.DeckWordsRequest;
import com.almonium.card.deck.dto.response.DeckDto;
import com.almonium.card.deck.service.DeckService;
import com.almonium.user.core.model.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Learning")
@RestController
@RequestMapping("/decks")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class DeckController {
    DeckService deckService;

    @GetMapping
    public ResponseEntity<List<DeckDto>> myDecks(@Auth User user) {
        return ResponseEntity.ok(deckService.myDecks(user));
    }

    @PostMapping
    public ResponseEntity<DeckDto> create(@Valid @RequestBody DeckCreationRequest request, @Auth User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deckService.createDeck(user, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeckDto> get(@PathVariable UUID id, @Auth User user) {
        return ResponseEntity.ok(deckService.getDeck(user, id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DeckDto> update(
            @PathVariable UUID id, @Valid @RequestBody DeckUpdateRequest request, @Auth User user) {
        return ResponseEntity.ok(deckService.updateDeck(user, id, request));
    }

    @PutMapping("/{id}/words")
    public ResponseEntity<DeckDto> setWords(
            @PathVariable UUID id, @Valid @RequestBody DeckWordsRequest request, @Auth User user) {
        return ResponseEntity.ok(deckService.setWords(user, id, request.wordIds()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, @Auth User user) {
        deckService.deleteDeck(user, id);
        return ResponseEntity.noContent().build();
    }
}
