package com.almonium.infra.chat.controller;

import com.almonium.auth.common.annotation.RequireRecentLogin;
import com.almonium.infra.chat.dto.request.AnnouncementRequest;
import com.almonium.infra.chat.service.StreamChatService;
import com.almonium.infra.chat.service.StreamUserReconciliationService;
import com.almonium.util.dto.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The operational side of chat: publishing to channels nobody else can write to, and the
 * maintenance that Stream state occasionally needs because it lives outside our database.
 */
@RestController
@RequestMapping("/ops/chat")
@RequiredArgsConstructor
public class OperationsChatController {
    private final StreamChatService streamChatService;
    private final StreamUserReconciliationService reconciliationService;

    @PostMapping("/announcements")
    @RequireRecentLogin
    public ResponseEntity<ApiResponse> publish(@Valid @RequestBody AnnouncementRequest request) {
        String messageId = streamChatService.publishAnnouncement(request.language(), request.text());
        return ResponseEntity.ok(new ApiResponse(true, "Published message " + messageId));
    }

    /** Re-stamps the system channels with the artwork the client currently serves. */
    @PostMapping("/system-channels/artwork")
    @RequireRecentLogin
    public ResponseEntity<ApiResponse> syncArtwork() {
        streamChatService.syncSystemChannelImages();
        return ResponseEntity.ok(new ApiResponse(true, "System channel artwork synced"));
    }

    /** Stream users with no row in our database - a dry run, so an operator can look before deleting. */
    @GetMapping("/orphans")
    public ResponseEntity<List<String>> findOrphans() {
        return ResponseEntity.ok(reconciliationService.findOrphans());
    }

    @DeleteMapping("/orphans")
    @RequireRecentLogin
    public ResponseEntity<ApiResponse> deleteOrphans() {
        List<String> deleted = reconciliationService.deleteOrphans();
        return ResponseEntity.ok(new ApiResponse(true, "Deleted " + deleted.size() + " orphaned Stream users"));
    }
}
