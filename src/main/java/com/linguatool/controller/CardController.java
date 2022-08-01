package com.linguatool.controller;

import com.linguatool.configuration.CurrentUser;
import com.linguatool.model.dto.LocalUser;
import com.linguatool.model.dto.api.request.CardCreationDto;
import com.linguatool.model.dto.api.request.CardDto;
import com.linguatool.model.mapping.CardMapper;
import com.linguatool.repository.CardRepository;
import com.linguatool.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/cards")
public class CardController {

    @Autowired
    UserServiceImpl userService;
    @Autowired
    CardRepository cardRepository;

    @Autowired
    CardMapper cardMapper;

    @PostMapping("create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createCard(@Valid @RequestBody CardCreationDto dto, @CurrentUser LocalUser userDetails) {
        userService.createCard(userDetails.getUser(), dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<CardDto>> getCardStack(@CurrentUser LocalUser user) {
        return ResponseEntity.ok(userService.getUsersCards(user.getUser()));
    }

    //TODO delete
    @GetMapping("{id}")
    public ResponseEntity<CardDto> getCard(@PathVariable Long id) {
        return ResponseEntity.of((cardRepository.findById(id)).map(e -> cardMapper.cardEntityToDto(e)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CardDto> deleteCard(@PathVariable Long id) {
        cardRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
