package com.almonium.infra.messaging.consumer;

import com.almonium.infra.storage.service.FirebaseStorageService;
import com.almonium.user.core.events.UserDeletedEvent;
import com.almonium.user.core.exception.FirebaseIntegrationException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserFirebaseAvatarCleanupListener {
    private final FirebaseStorageService firebaseStorageService;

    @RabbitListener(queues = "${rabbitmq.queue.user-deleted-firebase.name}")
    public void handleUserDeletedForFirebase(UserDeletedEvent event) {
        List<String> avatarPaths = event.avatarFilePaths();
        if (avatarPaths == null || avatarPaths.isEmpty()) {
            log.info("No Firebase avatar paths provided for user {}. Skipping Firebase cleanup.", event.userId());
            return;
        }

        List<String> failedDeletions = new ArrayList<>();
        List<String> successfulDeletions = new ArrayList<>();

        event.avatarFilePaths().forEach(filePath -> {
            try {
                firebaseStorageService.deleteFile(filePath);
                successfulDeletions.add(filePath);
                log.info("Successfully deleted Firebase file {} for deleted user {}", filePath, event.userId());
            } catch (FirebaseIntegrationException e) {
                log.warn(
                        "Firebase file deletion failed for path {} (user {}). Error: {}. Adding to failed list.",
                        filePath,
                        event.userId(),
                        e.getMessage());
                failedDeletions.add(filePath);
            } catch (Exception e) {
                log.error(
                        "Unexpected error during Firebase deletion for user {} path {}. Error: {}",
                        event.userId(),
                        filePath,
                        e.getMessage(),
                        e);
                failedDeletions.add(filePath);
            }
        });

        // Log the outcome but DO NOT THROW
        if (!failedDeletions.isEmpty()) {
            log.error(
                    "Firebase cleanup partially failed for user {}. {} successes, {} failures. Failures: {}",
                    event.userId(),
                    successfulDeletions.size(),
                    failedDeletions.size(),
                    failedDeletions);
        } else {
            log.info(
                    "Successfully cleaned up all {} Firebase avatars for deleted user {}",
                    successfulDeletions.size(),
                    event.userId());
        }
    }
}
