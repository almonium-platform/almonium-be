package com.almonium.infra.notification.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.config.properties.AppProperties;
import com.almonium.infra.notification.dto.request.FCMTokenRequest;
import com.almonium.infra.notification.model.entity.FCMToken;
import com.almonium.infra.notification.repository.FCMTokenRepository;
import com.almonium.user.core.model.entity.User;
import com.google.common.collect.Lists;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.SendResponse;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushFcmOptions;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FCMService {
    /** The data key every client reads to know where a tap goes. */
    public static final String PATH = "path";

    FCMTokenRepository fcmTokenRepository;
    AppProperties appProperties;

    public void registerToken(FCMTokenRequest request, User user) {
        fcmTokenRepository
                .findByToken(request.token())
                .ifPresentOrElse(
                        token -> {
                            if (!token.isActive()) {
                                token.setActive(true);
                                fcmTokenRepository.save(token);
                            }
                            token.setLastUsedAt(Instant.now());
                        },
                        () -> fcmTokenRepository.save(new FCMToken(user, request.token(), request.deviceType())));
    }

    public void sendNotificationToUser(UUID userId, String title, String message) {
        sendToUser(userId, title, message, null);
    }

    /**
     * One banner to every device the user already granted push on. {@code path} is the in-app page a tap opens:
     * it rides in the data payload for the native clients and as the WebPush link for the browser.
     */
    public void sendToUser(UUID userId, String title, String body, String path) {
        try {
            List<FCMToken> tokens = fcmTokenRepository.findByUserId(userId);
            if (tokens.isEmpty()) return;

            List<String> activeTokens = tokens.stream().map(FCMToken::getToken).toList();
            FirebaseMessaging.getInstance().sendEachForMulticast(message(activeTokens, title, body, path));
        } catch (FirebaseMessagingException e) {
            log.error("Error sending notification to user with id: {}. Error: {}", userId, e.getMessage());
        }
    }

    public void sendNotificationUsers(List<User> users, String title, String message) {
        try {
            List<String> allTokens = users.stream()
                    .flatMap(user -> fcmTokenRepository.findByUserId(user.getId()).stream())
                    .map(FCMToken::getToken)
                    .toList();

            List<List<String>> tokenBatches = Lists.partition(allTokens, 500);

            for (List<String> tokenBatch : tokenBatches) {
                BatchResponse response =
                        FirebaseMessaging.getInstance().sendEachForMulticast(message(tokenBatch, title, message, null));

                if (response.getFailureCount() > 0) {
                    log.warn(
                            "Failures sending mass notification to {} users: {}",
                            users.size(),
                            response.getFailureCount());

                    List<SendResponse> responses = response.getResponses();
                    for (int i = 0; i < responses.size(); i++) {
                        if (!responses.get(i).isSuccessful()) {
                            // Token i in tokenBatch failed. Remove corresponding registration token from database.
                            String failedToken = tokenBatch.get(i);
                            Optional<FCMToken> fcmTokenToDelete = fcmTokenRepository.findByToken(failedToken);
                            fcmTokenToDelete.ifPresent(fcmTokenRepository::delete); // Delete Token
                            log.warn(
                                    "Removing FCM Token {} Because Message Failed {}",
                                    failedToken,
                                    responses.get(i).getMessageId());
                        }
                    }
                }
            }
        } catch (FirebaseMessagingException e) {
            log.error("Error sending mass notification. UsersCount {} Error: {}", users.size(), e.getMessage());
        }
    }

    private MulticastMessage message(List<String> tokens, String title, String body, String path) {
        MulticastMessage.Builder builder = MulticastMessage.builder()
                .setNotification(
                        Notification.builder().setTitle(title).setBody(body).build())
                .addAllTokens(tokens);
        if (path != null && !path.isBlank()) {
            builder.putData(PATH, path)
                    .setWebpushConfig(WebpushConfig.builder()
                            .setFcmOptions(WebpushFcmOptions.withLink(appProperties.getWebDomain() + path))
                            .build());
        }
        return builder.build();
    }
}
