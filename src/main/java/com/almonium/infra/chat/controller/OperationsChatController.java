package com.almonium.infra.chat.controller;

import com.almonium.auth.common.annotation.RequireRecentLogin;
import com.almonium.infra.chat.dto.request.AnnouncementRequest;
import com.almonium.infra.chat.dto.request.PurgeRequest;
import com.almonium.infra.chat.service.StreamChatService;
import com.almonium.infra.chat.service.StreamPurgeService;
import com.almonium.infra.chat.service.StreamUserReconciliationService;
import com.almonium.util.dto.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
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
    private final StreamPurgeService purgeService;

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

    /** The phrase the purge demands, so the console can show an operator what they have to type. */
    @GetMapping("/purge")
    public ResponseEntity<Map<String, String>> purgeConfirmation() {
        return ResponseEntity.ok(Map.of("confirmation", purgeService.confirmationPhrase()));
    }

    /**
     * Empties the Stream application and rebuilds the broadcast channels. The confirmation phrase
     * names the environment, so the words that clear a scratch app do not clear the real one.
     */
    @PostMapping("/purge")
    @RequireRecentLogin
    public ResponseEntity<ApiResponse> purge(@Valid @RequestBody PurgeRequest request) {
        String expected = purgeService.confirmationPhrase();
        if (!expected.equals(request.confirmation())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Confirmation must read exactly: " + expected));
        }

        StreamPurgeService.PurgeSummary summary = purgeService.purgeEverything();
        return ResponseEntity.ok(new ApiResponse(
                true,
                String.format(
                        "Deleted %d channels and %d users; broadcast channels recreated",
                        summary.channelsDeleted(), summary.usersDeleted())));
    }
}
