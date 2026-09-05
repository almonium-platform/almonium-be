package com.almonium.infra.notification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.almonium.infra.notification.mapper.NotificationMapper;
import com.almonium.infra.notification.model.entity.Notification;
import com.almonium.infra.notification.repository.NotificationRepository;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.relationship.event.FriendshipEmailRequestedEvent;
import com.almonium.user.relationship.model.entity.Relationship;
import com.almonium.user.relationship.model.enums.FriendshipEvent;
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
        verify(fcmService).sendNotificationToUser(any(), any(), any());
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
        User user = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .email(username + "@example.com")
                .build();
        user.setProfile(Profile.builder()
                .user(user)
                .socialEmailNotifications(socialEmails)
                .build());
        return user;
    }

    private static Relationship relationship(User requester, User requestee) {
        Relationship relationship = new Relationship(requester, requestee);
        relationship.setId(UUID.randomUUID());
        return relationship;
    }
}
