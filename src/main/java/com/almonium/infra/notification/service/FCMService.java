package com.almonium.infra.notification.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.notification.dto.FCMTokenRequest;
import com.almonium.infra.notification.entity.FCMToken;
import com.almonium.infra.notification.repository.FCMTokenRepository;
import com.almonium.user.core.model.entity.User;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FCMService {
    FCMTokenRepository fcmTokenRepository;

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

    public void sendNotificationToUser(Long userId, String title, String message) throws FirebaseMessagingException {
        List<FCMToken> tokens = fcmTokenRepository.findByUserId(userId);
        if (tokens.isEmpty()) return;

        List<String> activeTokens = tokens.stream().map(FCMToken::getToken).toList();
        MulticastMessage multicastMessage = MulticastMessage.builder()
                .setNotification(
                        Notification.builder().setTitle(title).setBody(message).build())
                .addAllTokens(activeTokens)
                .build();

        FirebaseMessaging.getInstance().sendEachForMulticast(multicastMessage);
    }
}
