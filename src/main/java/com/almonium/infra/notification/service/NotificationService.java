package com.almonium.infra.notification.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.infra.notification.dto.response.NotificationDto;
import com.almonium.infra.notification.mapper.NotificationMapper;
import com.almonium.infra.notification.model.entity.Notification;
import com.almonium.infra.notification.model.enums.NotificationType;
import com.almonium.infra.notification.repository.NotificationRepository;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.relationship.event.FriendshipEmailRequestedEvent;
import com.almonium.user.relationship.model.entity.Relationship;
import com.almonium.user.relationship.model.enums.FriendshipEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class NotificationService {
    FCMService fcmService;

    NotificationRepository notificationRepository;

    NotificationMapper notificationMapper;
    ApplicationEventPublisher eventPublisher;

    public List<NotificationDto> getNotificationsForUser(User user) {
        return notificationMapper.toDto(notificationRepository.findByRecipientOrderByReadAtDescCreatedAtDesc(user));
    }

    @Transactional
    public void deleteNotification(User user, UUID id) {
        notificationRepository.deleteByIdAndRecipient(id, user);
    }

    @Transactional
    public void readAllNotifications(User user) {
        notificationRepository.readAllUnreadNotifications(user);
    }

    @Transactional
    public void readNotification(User user, UUID id) {
        notificationRepository.readNotification(user, id);
    }

    @Transactional
    public void unreadNotification(User user, UUID id) {
        notificationRepository.unreadNotification(user, id);
    }

    public void notifyOfTranslationOrderCompletion(String bookTitle, Language language, List<User> users) {
        String title = "Translation order completed";
        String message = "%s has been translated to %s".formatted(bookTitle, language);

        List<Notification> notifications = new ArrayList<>();

        for (User user : users) {
            Notification notification = Notification.builder()
                    .title(title)
                    .message(message)
                    .recipient(user)
                    .type(NotificationType.TRANSLATION_ORDER_COMPLETED)
                    .build();

            notifications.add(notification);
        }

        notificationRepository.saveAll(notifications);

        fcmService.sendNotificationUsers(users, title, message);
    }

    public void notifyOfFriendshipAcceptance(Relationship relationship) {
        String title = "Friendship request accepted";
        String message = "@%s accepted your friendship request!"
                .formatted(relationship.getRequestee().getUsername());

        Notification notification = Notification.builder()
                .title(title)
                .message(message)
                .recipient(relationship.getRequester())
                .sender(relationship.getRequestee())
                .type(NotificationType.FRIENDSHIP_ACCEPTED)
                .pictureUrl(relationship.getRequestee().getProfile().getAvatarUrl())
                .referenceId(relationship.getId())
                .build();

        notificationRepository.save(notification);

        fcmService.sendNotificationToUser(relationship.getRequester().getId(), title, message);

        eventPublisher.publishEvent(new FriendshipEmailRequestedEvent(
                relationship.getRequester().getId(),
                relationship.getRequester().getEmail(),
                relationship.getRequester().getUsername(),
                relationship.getRequestee().getUsername(),
                FriendshipEvent.ACCEPTED));
    }

    public void notifyFriendshipRequestRecipient(User initiator, User recipient, Relationship relationship) {
        String title = "Friendship request received";
        String message = "@%s wants to be friends with you!".formatted(initiator.getUsername());

        Notification notification = Notification.builder()
                .recipient(recipient)
                .sender(initiator)
                .title(title)
                .message(message)
                .type(NotificationType.FRIENDSHIP_REQUESTED)
                .pictureUrl(initiator.getProfile().getAvatarUrl())
                .referenceId(relationship.getId())
                .build();

        notificationRepository.save(notification);

        fcmService.sendNotificationToUser(recipient.getId(), title, message);

        eventPublisher.publishEvent(new FriendshipEmailRequestedEvent(
                recipient.getId(),
                recipient.getEmail(),
                recipient.getUsername(),
                initiator.getUsername(),
                FriendshipEvent.INITIATED));
    }
}
