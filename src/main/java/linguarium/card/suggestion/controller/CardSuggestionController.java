package linguarium.card.suggestion.controller;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.Valid;
import java.util.List;
import linguarium.auth.common.entity.Principal;
import linguarium.card.core.dto.CardDto;
import linguarium.card.suggestion.dto.CardSuggestionDto;
import linguarium.card.suggestion.service.CardSuggestionService;
import linguarium.util.annotation.CurrentUser;
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

@RestController
@RequestMapping("/cards/suggestions")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CardSuggestionController {
    CardSuggestionService cardSuggestionService;

    @PostMapping
    public ResponseEntity<?> suggestCard(@Valid @RequestBody CardSuggestionDto dto, @CurrentUser Principal auth) {
        cardSuggestionService.suggestCard(dto, auth.getUser().getLearner());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<Void> acceptCard(@PathVariable Long id, @CurrentUser Principal auth) {
        cardSuggestionService.acceptSuggestion(id, auth.getUser().getLearner());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/decline")
    public ResponseEntity<Void> declineCard(@PathVariable Long id, @CurrentUser Principal auth) {
        cardSuggestionService.declineSuggestion(id, auth.getUser().getLearner());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CardDto>> getSuggestedCardStack(@CurrentUser Principal auth) {
        return ResponseEntity.ok(
                cardSuggestionService.getSuggestedCards(auth.getUser().getLearner()));
    }
}
