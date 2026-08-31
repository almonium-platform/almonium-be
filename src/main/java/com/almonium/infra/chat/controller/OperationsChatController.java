package com.almonium.infra.chat.controller;

import com.almonium.auth.common.annotation.RequireRecentLogin;
import com.almonium.infra.chat.dto.request.AnnouncementRequest;
import com.almonium.infra.chat.service.StreamChatService;
import com.almonium.util.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The publishing side of the broadcast channels. Readers cannot write to them, so an announcement
 * has to come from the server, and the only hand on that lever is an admin's.
 */
@RestController
@RequestMapping("/ops/chat/announcements")
@RequiredArgsConstructor
public class OperationsChatController {
    private final StreamChatService streamChatService;

    @PostMapping
    @RequireRecentLogin
    public ResponseEntity<ApiResponse> publish(@Valid @RequestBody AnnouncementRequest request) {
        String messageId = streamChatService.publishAnnouncement(request.language(), request.text());
        return ResponseEntity.ok(new ApiResponse(true, "Published message " + messageId));
    }
}
