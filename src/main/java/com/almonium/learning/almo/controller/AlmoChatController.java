package com.almonium.learning.almo.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.annotation.Auth;
import com.almonium.learning.almo.dto.AlmoChatDto;
import com.almonium.learning.almo.dto.AlmoReplyDto;
import com.almonium.learning.almo.dto.AlmoReplyRequest;
import com.almonium.learning.almo.service.AlmoChatService;
import com.almonium.user.core.model.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Chat with Almo. The messages themselves travel through Stream like any other chat's; this is only the part Stream
 * cannot do - the channels' existence, and the reply.
 */
@Tag(name = "Almo", description = "The conversation partner who speaks from the learner's deck")
@RestController
@RequestMapping("/almo/chats")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AlmoChatController {
    AlmoChatService almoChatService;

    /** One channel per active target language for a member; nothing for a free account. Safe to call on every load. */
    @PostMapping
    public ResponseEntity<List<AlmoChatDto>> ensureChats(@Auth User user) {
        return ResponseEntity.ok(almoChatService.ensureChats(user));
    }

    /** Asks Almo to answer a message the learner just sent in that language's channel. */
    @PostMapping("/{language}/replies")
    public ResponseEntity<AlmoReplyDto> reply(
            @PathVariable Language language, @Valid @RequestBody AlmoReplyRequest request, @Auth User user) {
        return ResponseEntity.ok(almoChatService.reply(user, language, request.userMessageId()));
    }
}
