package com.almonium.learning.wanted.controller;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.annotation.Auth;
import com.almonium.learning.wanted.dto.request.WantRequest;
import com.almonium.learning.wanted.dto.response.WantReceipt;
import com.almonium.learning.wanted.dto.response.WantedQueueDto;
import com.almonium.learning.wanted.service.WantService;
import com.almonium.user.core.model.entity.User;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Wanting a book and the Wanted page (W1–W3). Members only: a guest is sent to sign in first. */
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class WantController {
    private final WantService wantService;

    @PostMapping("/wants")
    public ResponseEntity<WantReceipt> want(@Auth User user, @Valid @RequestBody WantRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(wantService.want(user, request));
    }

    @DeleteMapping("/wants/{catalogWorkId}/{language}")
    public ResponseEntity<Void> withdraw(
            @Auth User user, @PathVariable UUID catalogWorkId, @PathVariable Language language) {
        wantService.withdraw(user, catalogWorkId, language);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/wanted")
    public ResponseEntity<WantedQueueDto> wanted(@Auth User user, @RequestParam(defaultValue = "false") boolean mine) {
        return ResponseEntity.ok(wantService.queue(user, mine));
    }
}
