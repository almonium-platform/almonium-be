package com.almonium.card.deck.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.card.deck.dto.request.AddSharedWordsRequest;
import com.almonium.card.deck.dto.response.AddedWordsResult;
import com.almonium.card.deck.dto.response.SharedLinkViewerStatus;
import com.almonium.card.deck.service.SharedLinkService;
import com.almonium.user.core.model.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** The signed-in half of a shared link: what the viewer already holds, and copying words into their account. */
@Tag(name = "Sharing")
@RestController
@RequestMapping("/shares")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SharedLinkController {
    SharedLinkService sharedLinkService;

    @GetMapping("/cards/{publicId}/viewer")
    public ResponseEntity<SharedLinkViewerStatus> cardViewer(@PathVariable UUID publicId, @Auth User user) {
        return ResponseEntity.ok(sharedLinkService.cardViewer(user, publicId));
    }

    @PostMapping("/cards/{publicId}/words")
    public ResponseEntity<AddedWordsResult> addCard(@PathVariable UUID publicId, @Auth User user) {
        return ResponseEntity.ok(sharedLinkService.addCard(user, publicId));
    }

    @GetMapping("/decks/{shareId}/viewer")
    public ResponseEntity<SharedLinkViewerStatus> deckViewer(@PathVariable String shareId, @Auth User user) {
        return ResponseEntity.ok(sharedLinkService.deckViewer(user, shareId));
    }

    @PostMapping("/decks/{shareId}/words")
    public ResponseEntity<AddedWordsResult> addFromDeck(
            @PathVariable String shareId, @Valid @RequestBody AddSharedWordsRequest request, @Auth User user) {
        return ResponseEntity.ok(sharedLinkService.addFromDeck(user, shareId, request.wordIds()));
    }
}
