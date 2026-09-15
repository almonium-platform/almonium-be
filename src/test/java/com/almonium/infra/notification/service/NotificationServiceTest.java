package com.almonium.infra.notification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.infra.notification.event.NotificationReadEvent;
import com.almonium.infra.notification.mapper.NotificationMapper;
import com.almonium.infra.notification.model.entity.Notification;
import com.almonium.infra.notification.model.enums.NotificationType;
import com.almonium.infra.notification.repository.NotificationRepository;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.relationship.event.FriendshipEmailRequestedEvent;
import com.almonium.user.relationship.model.entity.Relationship;
import com.almonium.user.relationship.model.enums.FriendshipEvent;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    FCMService fcmService;

    @Mock
    NotificationRepository notificationRepository;

    @Mock
    NotificationMapper notificationMapper;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @InjectMocks
    NotificationService notificationService;

    @Test
    void connectionRequestEmailsTheRecipientWhoLeftSocialEmailsOn() {
        User initiator = user("marta", true);
        User recipient = user("oleg", true);

        notificationService.notifyFriendshipRequestRecipient(initiator, recipient, relationship(initiator, recipient));

        ArgumentCaptor<FriendshipEmailRequestedEvent> event =
                ArgumentCaptor.forClass(FriendshipEmailRequestedEvent.class);
        verify(eventPublisher).publishEvent(event.capture());
        assertThat(event.getValue().recipientEmail()).isEqualTo("oleg@example.com");
        assertThat(event.getValue().counterpartUsername()).isEqualTo("marta");
        assertThat(event.getValue().friendshipEvent()).isEqualTo(FriendshipEvent.INITIATED);
    }

    @Test
    void connectionRequestStillRingsTheBellWhenSocialEmailsAreOff() {
        User initiator = user("marta", true);
        User recipient = user("oleg", false);

        notificationService.notifyFriendshipRequestRecipient(initiator, recipient, relationship(initiator, recipient));

        verify(notificationRepository).save(any(Notification.class));
        // The switch covers email and push together; only the bell row lands.
        verify(fcmService, never()).sendNotificationToUser(any(), any(), any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void connectionRequestPushesWhenSocialNotificationsAreOn() {
        User initiator = user("marta", true);
        User recipient = user("oleg", true);

        notificationService.notifyFriendshipRequestRecipient(initiator, recipient, relationship(initiator, recipient));

        verify(fcmService).sendNotificationToUser(eq(recipient.getId()), any(), any());
    }

    @Test
    void fulfilmentRowCarriesTheBookTheTileAndThePath() {
        User reader = user("oleg", true);
        UUID orderId = UUID.randomUUID();

        notificationService.notifyOfTranslationReady(
                reader,
                orderId,
                "Effi Briest",
                Language.UK,
                "August",
                "https://covers.example/effi.jpg",
                "/reader/effi-briest-de-original?parallel=UK");

        ArgumentCaptor<Notification> saved = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository).save(saved.capture());
        Notification row = saved.getValue();
        assertThat(row.getType()).isEqualTo(NotificationType.TRANSLATION_ORDER_COMPLETED);
        assertThat(row.getTitle()).isEqualTo("Effi Briest now reads alongside Ukrainian");
        assertThat(row.getMessage()).isEqualTo("You asked for it in August.");
        assertThat(row.getReferenceId()).isEqualTo(orderId);
        assertThat(row.getContextTitle()).isEqualTo("Effi Briest");
        assertThat(row.getPictureUrl()).isEqualTo("https://covers.example/effi.jpg");
        assertThat(row.getActionPath()).isEqualTo("/reader/effi-briest-de-original?parallel=UK");
        assertThat(row.getSender()).isNull();
        verify(fcmService)
                .sendToUser(
                        reader.getId(),
                        "Effi Briest now reads alongside Ukrainian",
                        "The book you asked for in August is ready.",
                        "/reader/effi-briest-de-original?parallel=UK");
    }

    @Test
    void fulfilmentRowStillLandsWhenBookNotificationsAreOffButNothingIsPushed() {
        User reader = user("oleg", true, false);

        notificationService.notifyOfTranslationReady(
                reader, UUID.randomUUID(), "Effi Briest", Language.UK, "August", null, "/reader/effi?parallel=UK");

        verify(notificationRepository).save(any(Notification.class));
        verify(fcmService, never()).sendToUser(any(), any(), any(), any());
    }

    @Test
    void suggestionRowSaysSuggestedInsteadOfAsked() {
        User reader = user("oleg", true);
        UUID suggestionId = UUID.randomUUID();

        notificationService.notifyOfSuggestionPublished(
                reader, suggestionId, "Der Schimmelreiter", "August", null, "/books/der-schimmelreiter-de-original");

        ArgumentCaptor<Notification> saved = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository).save(saved.capture());
        assertThat(saved.getValue().getType()).isEqualTo(NotificationType.LIBRARY_SUGGESTION_PUBLISHED);
        assertThat(saved.getValue().getTitle()).isEqualTo("Der Schimmelreiter is now in the library");
        assertThat(saved.getValue().getMessage()).isEqualTo("You suggested it in August.");
        verify(fcmService)
                .sendToUser(
                        reader.getId(),
                        "Der Schimmelreiter is now in the library",
                        "The book you suggested in August is ready.",
                        "/books/der-schimmelreiter-de-original");
    }

    @Test
    void readingARowAnnouncesWhatItReferredTo() {
        User reader = user("oleg", true);
        UUID orderId = UUID.randomUUID();
        Notification row = Notification.builder()
                .id(UUID.randomUUID())
                .recipient(reader)
                .type(NotificationType.TRANSLATION_ORDER_COMPLETED)
                .referenceId(orderId)
                .build();
        when(notificationRepository.findByIdAndRecipient(row.getId(), reader)).thenReturn(Optional.of(row));

        notificationService.readNotification(reader, row.getId());

        verify(notificationRepository).readNotification(reader, row.getId());
        ArgumentCaptor<NotificationReadEvent> event = ArgumentCaptor.forClass(NotificationReadEvent.class);
        verify(eventPublisher).publishEvent(event.capture());
        assertThat(event.getValue().userId()).isEqualTo(reader.getId());
        assertThat(event.getValue().type()).isEqualTo(NotificationType.TRANSLATION_ORDER_COMPLETED);
        assertThat(event.getValue().referenceId()).isEqualTo(orderId);
    }

    @Test
    void readingAnAlreadyReadRowAnnouncesNothing() {
        User reader = user("oleg", true);
        Notification row = Notification.builder()
                .id(UUID.randomUUID())
                .recipient(reader)
                .type(NotificationType.TRANSLATION_ORDER_COMPLETED)
                .referenceId(UUID.randomUUID())
                .readAt(Instant.now())
                .build();
        when(notificationRepository.findByIdAndRecipient(row.getId(), reader)).thenReturn(Optional.of(row));

        notificationService.readNotification(reader, row.getId());

        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void markingAllReadAnnouncesEveryUnreadReference() {
        User reader = user("oleg", true);
        Notification withReference = Notification.builder()
                .recipient(reader)
                .type(NotificationType.LIBRARY_SUGGESTION_PUBLISHED)
                .referenceId(UUID.randomUUID())
                .build();
        Notification withoutReference = Notification.builder()
                .recipient(reader)
                .type(NotificationType.FRIENDSHIP_ACCEPTED)
                .build();
        when(notificationRepository.findByRecipientAndReadAtIsNull(reader))
                .thenReturn(List.of(withReference, withoutReference));

        notificationService.readAllNotifications(reader);

        verify(notificationRepository).readAllUnreadNotifications(reader);
        verify(eventPublisher, times(1)).publishEvent(any(NotificationReadEvent.class));
    }

    @Test
    void readByReferenceMarksTheRowWithoutAnnouncingIt() {
        User reader = user("oleg", true);
        UUID orderId = UUID.randomUUID();

        notificationService.readByReference(reader, orderId);

        verify(notificationRepository).readByRecipientAndReference(reader, orderId);
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void acceptanceEmailHonoursTheRequestersSwitchNotTheAccepters() {
        User requester = user("oleg", false);
        User requestee = user("marta", true);

        notificationService.notifyOfFriendshipAcceptance(relationship(requester, requestee));

        verify(eventPublisher, never()).publishEvent(any());
    }

    private static User user(String username, boolean socialEmails) {
        return user(username, socialEmails, true);
    }

    private static User user(String username, boolean socialEmails, boolean bookEmails) {
        User user = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .email(username + "@example.com")
                .build();
        user.setProfile(Profile.builder()
                .user(user)
                .socialEmailNotifications(socialEmails)
                .bookEmailNotifications(bookEmails)
                .build());
        return user;
    }

    private static Relationship relationship(User requester, User requestee) {
        Relationship relationship = new Relationship(requester, requestee);
        relationship.setId(UUID.randomUUID());
        return relationship;
    }
}
