package com.almonium.infra.notification.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.infra.email.service.FriendshipEmailComposerService;
import com.almonium.infra.notification.dto.response.NotificationDto;
import com.almonium.infra.notification.mapper.NotificationMapper;
import com.almonium.infra.notification.model.entity.Notification;
import com.almonium.infra.notification.model.enums.NotificationType;
import com.almonium.infra.notification.repository.NotificationRepository;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.friendship.model.entity.Friendship;
import com.almonium.user.friendship.model.enums.FriendshipEvent;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class NotificationService {
    NotificationRepository notificationRepository;
    FriendshipEmailComposerService friendshipEmailComposerService;
    FCMService fcmService;

    NotificationMapper notificationMapper;

    public List<NotificationDto> getNotificationsForUser(User user) {
        return notificationMapper.toDto(notificationRepository.findByUserOrderByReadAtDescCreatedAtDesc(user));
    }

    @Transactional
    public void deleteNotification(User user, UUID id) {
        notificationRepository.deleteByIdAndUser(id, user);
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

    public void notifyOfFriendshipAcceptance(Friendship friendship) {
        String title = "Friendship request accepted";
        String message = "%s accepted your friendship request!"
                .formatted(friendship.getRequestee().getUsername());

        Notification notification = Notification.builder()
                .user(friendship.getRequester())
                .title(title)
                .message(message)
                .type(NotificationType.FRIENDSHIP_ACCEPTED)
                .pictureUrl(friendship.getRequestee().getProfile().getAvatarUrl())
                .referenceId(friendship.getId())
                .build();

        notificationRepository.save(notification);

        fcmService.sendNotificationToUser(friendship.getRequester().getId(), title, message);

        friendshipEmailComposerService.sendEmail(
                friendship.getRequester().getUsername(),
                friendship.getRequester().getEmail(),
                new EmailContext<>(
                        FriendshipEvent.ACCEPTED,
                        Map.of(
                                FriendshipEmailComposerService.COUNTERPART_USERNAME,
                                friendship.getRequestee().getUsername())));
    }

    public void notifyFriendshipRequestRecipient(User initiator, User recipient, Friendship friendship) {
        String title = "Friendship request received";
        String message = "%s wants to be friends with you!".formatted(initiator.getUsername());

        Notification notification = Notification.builder()
                .user(recipient)
                .title(title)
                .message(message)
                .type(NotificationType.FRIENDSHIP_REQUESTED)
                .pictureUrl(initiator.getProfile().getAvatarUrl())
                .referenceId(friendship.getId())
                .build();

        notificationRepository.save(notification);

        fcmService.sendNotificationToUser(recipient.getId(), title, message);

        friendshipEmailComposerService.sendEmail(
                recipient.getUsername(),
                recipient.getEmail(),
                new EmailContext<>(
                        FriendshipEvent.INITIATED,
                        Map.of(FriendshipEmailComposerService.COUNTERPART_USERNAME, initiator.getUsername())));
    }
}
