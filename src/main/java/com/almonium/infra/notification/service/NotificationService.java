package com.almonium.infra.notification.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.analyzer.translator.util.LanguageNames;
import com.almonium.infra.notification.dto.response.NotificationDto;
import com.almonium.infra.notification.event.NotificationReadEvent;
import com.almonium.infra.notification.mapper.NotificationMapper;
import com.almonium.infra.notification.model.entity.Notification;
import com.almonium.infra.notification.model.enums.NotificationType;
import com.almonium.infra.notification.repository.NotificationRepository;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.relationship.event.FriendshipEmailRequestedEvent;
import com.almonium.user.relationship.model.entity.Relationship;
import com.almonium.user.relationship.model.enums.FriendshipEvent;
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
        List<Notification> unread = notificationRepository.findByRecipientAndReadAtIsNull(user);
        notificationRepository.readAllUnreadNotifications(user);
        unread.forEach(notification -> announceRead(user, notification));
    }

    @Transactional
    public void readNotification(User user, UUID id) {
        Notification notification =
                notificationRepository.findByIdAndRecipient(id, user).orElse(null);
        notificationRepository.readNotification(user, id);
        if (notification != null && notification.getReadAt() == null) {
            announceRead(user, notification);
        }
    }

    /** The other direction: the thing a row refers to was opened elsewhere, so the row is read too. */
    @Transactional
    public void readByReference(User user, UUID referenceId) {
        notificationRepository.readByRecipientAndReference(user, referenceId);
    }

    private void announceRead(User user, Notification notification) {
        if (notification.getReferenceId() == null) {
            return;
        }
        eventPublisher.publishEvent(
                new NotificationReadEvent(user.getId(), notification.getType(), notification.getReferenceId()));
    }

    @Transactional
    public void unreadNotification(User user, UUID id) {
        notificationRepository.unreadNotification(user, id);
    }

    /**
     * The one moment a message from us is wanted: the translation a reader asked for is published. One row per
     * reader, never collapsed; the tile shows the book, the tap opens chapter one of the pair. Push goes only to
     * devices that already said yes, and only while the Books switch is on; the bell row always lands.
     */
    public void notifyOfTranslationReady(
            User user,
            UUID orderId,
            String bookTitle,
            Language language,
            String monthAsked,
            String coverUrl,
            String actionPath) {
        String title = "%s now reads alongside %s".formatted(bookTitle, LanguageNames.englishName(language));
        String message = "You asked for it in %s.".formatted(monthAsked);
        Notification notification = Notification.builder()
                .title(title)
                .message(message)
                .recipient(user)
                .type(NotificationType.TRANSLATION_ORDER_COMPLETED)
                .referenceId(orderId)
                .pictureUrl(coverUrl)
                .contextTitle(bookTitle)
                .actionPath(actionPath)
                .build();
        notificationRepository.save(notification);
        pushBook(user, title, "The book you asked for in %s is ready.".formatted(monthAsked), actionPath);
    }

    /** The same shape with the word "suggested": a private import the reader suggested is in the library. */
    public void notifyOfSuggestionPublished(
            User user, UUID suggestionId, String bookTitle, String monthSuggested, String coverUrl, String actionPath) {
        String title = "%s is now in the library".formatted(bookTitle);
        String message = "You suggested it in %s.".formatted(monthSuggested);
        Notification notification = Notification.builder()
                .title(title)
                .message(message)
                .recipient(user)
                .type(NotificationType.LIBRARY_SUGGESTION_PUBLISHED)
                .referenceId(suggestionId)
                .pictureUrl(coverUrl)
                .contextTitle(bookTitle)
                .actionPath(actionPath)
                .build();
        notificationRepository.save(notification);
        pushBook(user, title, "The book you suggested in %s is ready.".formatted(monthSuggested), actionPath);
    }

    /** The book a member asked for (G19) is on the shelf; the row opens it. */
    public void notifyOfRequestPublished(
            User user, UUID requestId, String bookTitle, String monthAsked, String coverUrl, String actionPath) {
        String title = "%s is now on the shelf".formatted(bookTitle);
        String message = "You asked for it in %s.".formatted(monthAsked);
        Notification notification = Notification.builder()
                .title(title)
                .message(message)
                .recipient(user)
                .type(NotificationType.BOOK_REQUEST_PUBLISHED)
                .referenceId(requestId)
                .pictureUrl(coverUrl)
                .contextTitle(bookTitle)
                .actionPath(actionPath)
                .build();
        notificationRepository.save(notification);
        pushBook(user, title, "The book you asked for in %s is ready.".formatted(monthAsked), actionPath);
    }

    private void pushBook(User user, String title, String body, String actionPath) {
        if (!user.getProfile().isBookEmailNotifications()) {
            log.info("Skipping book push for user {}: book notifications are off", user.getId());
            return;
        }
        fcmService.sendToUser(user.getId(), title, body, actionPath);
    }

    public void notifyOfBookImport(User user, UUID importId, String bookTitle, boolean ready) {
        String title = ready ? "Your book is ready" : "Book import failed";
        String message =
                ready ? "%s is ready to read".formatted(bookTitle) : "We could not process %s".formatted(bookTitle);
        Notification notification = Notification.builder()
                .title(title)
                .message(message)
                .recipient(user)
                .type(ready ? NotificationType.BOOK_IMPORT_READY : NotificationType.BOOK_IMPORT_FAILED)
                .referenceId(importId)
                .build();
        notificationRepository.save(notification);
        fcmService.sendNotificationToUser(user.getId(), title, message);
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

        pushSocial(relationship.getRequester(), title, message);

        requestConnectionEmail(
                relationship.getRequester(), relationship.getRequestee().getUsername(), FriendshipEvent.ACCEPTED);
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

        pushSocial(recipient, title, message);

        requestConnectionEmail(recipient, initiator.getUsername(), FriendshipEvent.INITIATED);
    }

    /** The bell row always lands; the switch covers email and push together. */
    private void pushSocial(User recipient, String title, String message) {
        if (!recipient.getProfile().isSocialEmailNotifications()) {
            log.info("Skipping social push for user {}: social notifications are off", recipient.getId());
            return;
        }
        fcmService.sendNotificationToUser(recipient.getId(), title, message);
    }

    /** The bell row always lands; the switch covers email and push together. */
    private void requestConnectionEmail(User recipient, String counterpartUsername, FriendshipEvent event) {
        if (!recipient.getProfile().isSocialEmailNotifications()) {
            log.info("Skipping {} connection email for user {}: social emails are off", event, recipient.getId());
            return;
        }
        eventPublisher.publishEvent(new FriendshipEmailRequestedEvent(
                recipient.getId(), recipient.getEmail(), recipient.getUsername(), counterpartUsername, event));
    }
}
