package com.almonium.infra.chat.service;

import static io.getstream.chat.java.models.User.createToken;
import static io.getstream.chat.java.models.User.upsert;
import static lombok.AccessLevel.PRIVATE;

import com.almonium.config.properties.AppProperties;
import com.almonium.user.core.exception.StreamIntegrationException;
import com.almonium.user.core.model.entity.User;
import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.Channel;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class StreamChatService {
    private static final String SELF_CHAT_NAME = "Saved Messages";
    private static final String SELF_CHAT_TYPE = "messaging";
    private static final String SELF_CHAT_ID_TEMPLATE = "self_%s";

    private static final String READ_ONLY_CHAT_TYPE = "broadcast";

    private static final String SHORT_LINK_DOMAIN = "go.almonium.com";
    private static final String SHORT_LINK_TEMPLATE = "http://%s/%s";

    AppProperties appProperties;

    public String setupNewUser(User user) {
        createStreamUser(user);
        joinDefaultChannels(user);
        createSelfChat(user);
        return generateStreamToken(user);
    }

    public void joinDefaultChannels(User user) {
        try {
            Channel.update(READ_ONLY_CHAT_TYPE, getDefaultChannelId())
                    .addMember(String.valueOf(user.getId()))
                    .request();
        } catch (StreamException e) {
            throw new StreamIntegrationException("Error while joining default channel: " + e.getMessage(), e);
        }
    }

    public void createStreamUser(User user) {
        try {
            upsert().user(io.getstream.chat.java.models.User.UserRequestObject.builder()
                            .id(user.getId().toString())
                            .name(user.getUsername())
                            .additionalField("email", user.getEmail())
                            .build())
                    .request();
        } catch (StreamException e) {
            throw new StreamIntegrationException(
                    String.format("Error while creating user with id: %s, message: %s", user.getId(), e.getMessage()),
                    e);
        }
    }

    public String generateStreamToken(User user) {
        return createToken(String.valueOf(user.getId()), null, null);
    }

    public void updateUser(User user) {
        try {
            io.getstream.chat.java.models.User.UserRequestObject userRequest =
                    io.getstream.chat.java.models.User.UserRequestObject.builder()
                            .id(String.valueOf(user.getId())) // User ID to update
                            .name(user.getUsername()) // User name
                            .additionalField("email", user.getEmail()) // User email
                            .additionalField("image", user.getProfile().getAvatarUrl()) // New avatar URL
                            .build();

            // Upsert the user with the new avatar URL
            upsert().user(userRequest).request();

        } catch (StreamException e) {
            throw new StreamIntegrationException(
                    String.format("Error while updating user with id: %s, %s", user.getId(), e.getMessage()), e);
        }
    }

    public void createSelfChat(User user) {
        try {
            String selfChatId = String.format(SELF_CHAT_ID_TEMPLATE, user.getId());

            Channel.ChannelMemberRequestObject selfMember = Channel.ChannelMemberRequestObject.builder()
                    .userId(user.getId().toString())
                    .build();

            Channel.getOrCreate(SELF_CHAT_TYPE, selfChatId)
                    .data(Channel.ChannelRequestObject.builder()
                            .createdBy(io.getstream.chat.java.models.User.UserRequestObject.builder()
                                    .id(user.getId().toString())
                                    .name(user.getUsername())
                                    .build())
                            .members(Collections.singletonList(selfMember))
                            .additionalField("name", SELF_CHAT_NAME)
                            .additionalField("image", getShortLink("saved-messages"))
                            .build())
                    .request();

        } catch (StreamException e) {
            throw new StreamIntegrationException(
                    String.format("Error while creating self chat for user: %s, %s", user.getId(), e.getMessage()), e);
        }
    }

    private String getShortLink(String key) {
        return String.format(SHORT_LINK_TEMPLATE, SHORT_LINK_DOMAIN, key);
    }

    private String getDefaultChannelId() {
        return appProperties.getName().toLowerCase();
    }
    // should be run once, on project migration
    @SuppressWarnings("unused")
    private void createDefaultChannel() { // Fetch the channel details
        try {
            String defaultChannelId = getDefaultChannelId();

            Channel.getOrCreate(READ_ONLY_CHAT_TYPE, defaultChannelId)
                    .data(Channel.ChannelRequestObject.builder()
                            .createdBy(io.getstream.chat.java.models.User.UserRequestObject.builder()
                                    .id(defaultChannelId)
                                    .build())
                            .additionalField("image", getShortLink("logo"))
                            .additionalField("name", appProperties.getName())
                            .build())
                    .request();
        } catch (StreamException e) {
            throw new StreamIntegrationException("Error while creating default channel: " + e.getMessage(), e);
        }
    }
}
