package linguarium.card.core.controller;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.Valid;
import java.util.List;
import linguarium.auth.common.entity.Principal;
import linguarium.card.core.dto.CardCreationDto;
import linguarium.card.core.dto.CardDto;
import linguarium.card.core.dto.CardUpdateDto;
import linguarium.card.core.service.CardService;
import linguarium.util.annotation.CurrentUser;
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
    public ResponseEntity<Void> createCard(@Valid @RequestBody CardCreationDto dto, @CurrentUser Principal auth) {
        cardService.createCard(auth.getUser().getLearner(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCard(
            @PathVariable Long id, @Valid @RequestBody CardUpdateDto dto, @CurrentUser Principal auth) {
        cardService.updateCard(id, dto, auth.getUser().getLearner());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CardDto>> getCardStack(@CurrentUser Principal auth) {
        return ResponseEntity.ok(cardService.getUsersCards(auth.getUser().getLearner()));
    }

    @GetMapping("/lang/{code}")
    public ResponseEntity<List<CardDto>> getCardStackOfLang(@PathVariable String code, @CurrentUser Principal auth) {
        return ResponseEntity.ok(
                cardService.getUsersCardsOfLang(code, auth.getUser().getLearner()));
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
