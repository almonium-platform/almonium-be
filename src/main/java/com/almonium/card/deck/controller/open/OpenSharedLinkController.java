package com.almonium.card.deck.controller.open;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.card.deck.dto.response.SharedCardView;
import com.almonium.card.deck.dto.response.SharedDeckView;
import com.almonium.card.deck.service.SharedLinkService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** What anyone holding a link sees. A dead link still answers 200 with its status: the id existed. */
@Tag(name = "Sharing")
@RestController
@RequestMapping("/public/shares")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class OpenSharedLinkController {
    SharedLinkService sharedLinkService;

    @GetMapping("/cards/{publicId}")
    public ResponseEntity<SharedCardView> card(@PathVariable UUID publicId) {
        return ResponseEntity.ok(sharedLinkService.viewCard(publicId));
    }

    @GetMapping("/decks/{shareId}")
    public ResponseEntity<SharedDeckView> deck(@PathVariable String shareId) {
        return ResponseEntity.ok(sharedLinkService.viewDeck(shareId));
    }
}
